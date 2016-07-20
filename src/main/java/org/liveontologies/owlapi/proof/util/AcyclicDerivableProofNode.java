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

import java.util.HashSet;
import java.util.Set;

class AcyclicDerivableProofNode<C> extends DerivableProofNode<C> {

	private final AcyclicDerivableProofNode<C> parent_;

	AcyclicDerivableProofNode(ProofNode<C> delegate, AcyclicDerivableProofNode<C> parent) {
		super(delegate);
		this.parent_ = parent;
	}

	AcyclicDerivableProofNode(ProofNode<C> delegate) {
		super(delegate);
		this.parent_ = null;
	}

	@Override
	DerivabilityChecker<ProofNode<C>> getDerivabilityChecker() {
		return new ProofNodeDerivabilityChecker<C>(getBlockedNodes());
	}

	@Override
	ProofStep<C> convert(ProofStep<C> inference) {
		return new AcyclicDerivableProofStep<C>(inference, this);
	}

	protected Set<ProofNode<C>> getBlockedNodes() {
		Set<ProofNode<C>> result = new HashSet<ProofNode<C>>();
		AcyclicDerivableProofNode<C> next = this;
		do {
			// the original nodes that should be disregarded for checking
			// derivability
			result.add(next.getDelegate());
			next = next.parent_;
		} while (next != null);
		return result;
	}

}
