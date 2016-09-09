package org.liveontologies.owlapi.proof.util;

/*-
 * #%L
 * OWL API Proof Extension
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility to check derivability of conclusions by inferences (with optional
 * set of "forbidden" conclusions that should not appear in proofs). A
 * conclusion is derivable if it is a conclusion of an inference whose all
 * premises are derivable.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 *            the type of conclusions supported by this checker
 */
public class InferenceDerivabilityChecker<C> implements DerivabilityChecker<C> {

	// logger for this class
	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(InferenceDerivabilityChecker.class);

	/**
	 * the inferences that can be used for deriving conclusions
	 */
	private final InferenceSet<C> inferences_;

	/**
	 * conclusions for which a derivability test was initiated or finished
	 */
	private final Set<C> goals_ = new HashSet<C>();

	/**
	 * {@link #goals_} that are not yet checked for derivability
	 */
	private final Queue<C> toCheck_ = new LinkedList<C>();

	/**
	 * {@link #goals_} that that were found derivable
	 */
	private final Set<C> derivable_ = new HashSet<C>();

	/**
	 * {@link #derivable_} goals not yet used to derived other {@link #goals_}
	 */
	private final Queue<C> toPropagate_ = new LinkedList<C>();

	/**
	 * a map from {@link #toCheck_} goals to the list of inferences in which
	 * this goal can be used as a premise; these inferences are "waiting" for
	 * this conclusion to be derived
	 */
	private final Map<C, List<Inference<C>>> watchedInferences_ = new HashMap<C, List<Inference<C>>>();

	/**
	 * a map from {@link #toCheck_} goals to the iterator over the premises of
	 * the corresponding inference in {@link #watchedInferences_} that currently
	 * points to this goal (as it is one of the premises)
	 */
	private final Map<C, List<Iterator<? extends C>>> iterators_ = new HashMap<C, List<Iterator<? extends C>>>();

	public InferenceDerivabilityChecker(InferenceSet<C> inferences) {
		this.inferences_ = inferences;
	}

	public InferenceDerivabilityChecker(final InferenceSet<C> inferences,
			final Set<? extends C> forbidden) {
		this(new InferenceSet<C>() {
			@Override
			public Collection<? extends Inference<C>> getInferences(
					C conclusion) {
				if (forbidden.contains(conclusion)) {
					return Collections.emptySet();
				}
				// else
				Collection<? extends Inference<C>> original = inferences
						.getInferences(conclusion);
				// filtering out inferences with forbidden premises
				Collection<Inference<C>> result = new ArrayList<Inference<C>>(
						original.size());
				inference_loop: for (Inference<C> inf : original) {
					for (C premise : inf.getPremises()) {
						if (forbidden.contains(premise)) {
							LOGGER_.trace("{}: ignored: {} is forbiden", inf,
									premise);
							continue inference_loop;
						}
					}
					result.add(inf);
				}
				if (result.size() == original.size()) {
					// nothing was removed
					return original;
				}
				// else
				return result;
			}
		});
	}

	@Override
	public boolean isDerivable(C conclusion) {
		LOGGER_.trace("{}: checking derivability", conclusion);
		toCheck(conclusion);
		process();
		boolean derivable = derivable_.contains(conclusion);
		LOGGER_.trace("{}: derivable: {}", conclusion, derivable);
		return derivable;
	}

	private void process() {
		for (;;) {
			C next = toCheck_.poll();

			if (next != null) {
				for (Inference<C> inf : inferences_.getInferences(next)) {
					LOGGER_.trace("{}: expanding", inf);
					check(inf.getPremises().iterator(), inf);
				}
				continue;
			}

			next = toPropagate_.poll();

			if (next != null) {
				List<Inference<C>> watched = watchedInferences_.remove(next);
				if (watched == null) {
					continue;
				}
				List<Iterator<? extends C>> premiseIterators = iterators_
						.remove(next);
				for (int i = 0; i < watched.size(); i++) {
					Inference<C> inf = watched.get(i);
					Iterator<? extends C> iterator = premiseIterators.get(i);
					check(iterator, inf);
				}
				continue;
			}

			// all done
			return;

		}

	}

	private void toCheck(C conclusion) {
		if (goals_.add(conclusion)) {
			LOGGER_.trace("{}: new goal", conclusion);
			toCheck_.add(conclusion);
		}
	}

	private void addWatch(C premise, Iterator<? extends C> premiseIterator,
			Inference<C> inf) {
		List<Inference<C>> inferences = watchedInferences_.get(premise);
		List<Iterator<? extends C>> premiseIterators = iterators_.get(premise);
		if (inferences == null) {
			inferences = new ArrayList<Inference<C>>();
			watchedInferences_.put(premise, inferences);
			premiseIterators = new ArrayList<Iterator<? extends C>>();
			iterators_.put(premise, premiseIterators);
		}
		inferences.add(inf);
		premiseIterators.add(premiseIterator);
		toCheck(premise);
	}

	private void proved(C conclusion) {
		if (derivable_.add(conclusion)) {
			LOGGER_.trace("{}: derived", conclusion);
			toPropagate_.add(conclusion);
		}
	}

	private void check(Iterator<? extends C> premiseIterator,
			Inference<C> inf) {
		while (premiseIterator.hasNext()) {
			C next = premiseIterator.next();
			if (!derivable_.contains(next)) {
				addWatch(next, premiseIterator, inf);
				return;
			}
		}
		// all premises are derived
		LOGGER_.trace("{}: fire", inf);
		proved(inf.getConclusion());
	}

}
