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

class ProofNodeDerivabilityFromChecker<C>
		extends ProofNodeDerivabilityChecker<C> {

	private final Set<? extends C> statedAxioms_;

	ProofNodeDerivabilityFromChecker(Set<? extends C> statedAxioms) {
		this.statedAxioms_ = statedAxioms;
	}

	ProofNodeDerivabilityFromChecker(Set<ProofNode<C>> forbiddenNodes,
			Set<? extends C> statedAxioms) {
		super(extend(forbiddenNodes, statedAxioms));
		this.statedAxioms_ = statedAxioms;
	}

	private static <C> ExtendedProofNode<C> extend(ProofNode<C> node,
			Set<? extends C> statedAxioms) {
		return new ExtendedProofNode<C>(node, statedAxioms);
	}

	private static <C> Set<ExtendedProofNode<C>> extend(Set<ProofNode<C>> nodes,
			Set<? extends C> statedAxioms) {
		Set<ExtendedProofNode<C>> result = new HashSet<ExtendedProofNode<C>>(
				nodes.size());
		for (ProofNode<C> node : nodes) {
			result.add(extend(node, statedAxioms));
		}
		return result;
	}

	@Override
	public boolean isDerivable(ProofNode<C> node) {
		return super.isDerivable(extend(node, statedAxioms_));
	}

}
