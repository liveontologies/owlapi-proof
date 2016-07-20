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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InferenceDerivabilityChecker<C> implements DerivabilityChecker<C> {

	// logger for this class
	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(InferenceDerivabilityChecker.class);

	private final InferenceSet<C> inferences_;

	private final Set<? extends C> forbiddenConclusions_;

	private final Deque<C> conclusionStack_ = new ArrayDeque<C>();

	private final Deque<Iterator<? extends Inference<C>>> inferenceIteratorStack_ = new ArrayDeque<Iterator<? extends Inference<C>>>();

	private final Deque<Iterator<? extends C>> conclusionIteratorStack_ = new ArrayDeque<Iterator<? extends C>>();

	private final Set<C> derivable_ = new HashSet<C>();

	private final Set<C> checked_ = new HashSet<C>();

	public InferenceDerivabilityChecker(InferenceSet<C> inferences,
			Set<? extends C> forbiddenConclusions) {
		this.inferences_ = inferences;
		this.forbiddenConclusions_ = forbiddenConclusions;
	}

	public InferenceDerivabilityChecker(InferenceSet<C> inferences) {
		this(inferences, Collections.<C> emptySet());
	}

	@Override
	public boolean isDerivable(C conclusion) {
		if (forbiddenConclusions_.contains(conclusion)) {
			return false;
		}
		// else
		if (checked_.contains(conclusion)) {
			return derivable_.contains(conclusion);
		}
		// else
		conclusionStack_.push(conclusion);
		LOGGER_.trace("{}: to check", conclusion);
		return derivable();
	}

	boolean derivable() {
		first_loop: for (;;) {
			C nextConclusion = conclusionStack_.peek();
			inferenceIteratorStack_
					.push(inferences_.getInferences(nextConclusion).iterator());
			second_loop: for (;;) {
				Iterator<? extends Inference<C>> infIter = inferenceIteratorStack_
						.peek();
				if (infIter.hasNext()) {
					Collection<? extends C> premises = infIter.next()
							.getPremises();
					LOGGER_.trace("{}: check premises", premises);
					conclusionIteratorStack_.push(premises.iterator());
				} else {
					// conclusion not derivable
					inferenceIteratorStack_.pop();
					nextConclusion = conclusionStack_.pop();
					LOGGER_.trace("{}: not provable", nextConclusion);
					if (conclusionIteratorStack_.peek() == null) {
						return false;
					} else {
						conclusionIteratorStack_.pop();
						continue second_loop;
					}
				}
				third_loop: for (;;) {
					Iterator<? extends C> conclIter = conclusionIteratorStack_
							.peek();
					if (conclIter == null) {
						return true;
					}
					if (conclIter.hasNext()) {
						nextConclusion = conclIter.next();
						if (forbiddenConclusions_.contains(nextConclusion)) {
							// conclusion cannot be used
							LOGGER_.trace("{}: not provable [forbidden]",
									nextConclusion);
							conclusionIteratorStack_.pop();
							continue second_loop;
						} else if (derivable_.contains(nextConclusion)) {
							// derivable
							LOGGER_.trace("{}: provable [before]",
									nextConclusion);
							continue third_loop;
						} else if (checked_.add(nextConclusion)) {
							// new
							conclusionStack_.push(nextConclusion);
							LOGGER_.trace("{}: to check", nextConclusion);
							continue first_loop;
						} else {
							// already on stack or not derivable
							LOGGER_.trace("{}: not provable [before]",
									nextConclusion);
							conclusionIteratorStack_.pop();
							continue second_loop;
						}
					} else {
						// all conclusions proved
						conclusionIteratorStack_.pop();
						inferenceIteratorStack_.pop();
						nextConclusion = conclusionStack_.pop();
						derivable_.add(nextConclusion);
						LOGGER_.trace("{}: provable", nextConclusion);
						continue third_loop;
					}
				}
			}
		}
	}

}
