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

import java.util.Set;

class DerivableFromProofNode<C> extends DerivableProofNode<C> {

	private final Set<? extends C> statedAxioms_;

	DerivableFromProofNode(ProofNode<C> delegate,
			Set<? extends C> statedAxioms) {
		super(delegate);
		this.statedAxioms_ = statedAxioms;
	}

	@Override
	DerivabilityChecker<ProofNode<C>> getDerivabilityChecker() {
		return new ProofNodeDerivabilityFromChecker<C>(statedAxioms_);
	}

	@Override
	ProofStep<C> convert(ProofStep<C> inference) {
		return new DerivableFromProofStep<C>(inference, this, statedAxioms_);
	}

}
