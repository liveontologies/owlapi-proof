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
import java.util.List;

class DerivableProofNode<C> extends DelegatingProofNode<C> {

	DerivableProofNode(ProofNode<C> delegate) {
		super(delegate);
	}

	DerivabilityChecker<ProofNode<C>> getDerivabilityChecker() {
		return new ProofNodeDerivabilityChecker<C>();
	}

	ProofStep<C> convert(ProofStep<C> inference) {
		return new DerivableProofStep<C>(inference, this);
	}

	@Override
	public List<? extends ProofStep<C>> getInferences() {
		// converting original inferences that have only derivable premises
		List<ProofStep<C>> result = new ArrayList<ProofStep<C>>();
		DerivabilityChecker<ProofNode<C>> checker_ = getDerivabilityChecker();
		inference_loop: for (ProofStep<C> inference : getDelegate()
				.getInferences()) {
			for (ProofNode<C> premise : inference.getPremises()) {
				if (!checker_.isDerivable(premise)) {
					continue inference_loop;
				}
			}
			result.add(convert(inference));
		}
		return result;
	}

}
