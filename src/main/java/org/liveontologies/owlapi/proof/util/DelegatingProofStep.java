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

import java.util.Collection;

public class DelegatingProofStep<C> implements ProofStep<C> {

	private final ProofStep<C> delegate_;

	protected DelegatingProofStep(ProofStep<C> delegate) {
		this.delegate_ = delegate;
	}

	protected ProofStep<C> getDelegate() {
		return delegate_;
	}

	@Override
	public String getName() {
		return delegate_.getName();
	}

	@Override
	public ProofNode<C> getConclusion() {
		return delegate_.getConclusion();
	}

	@Override
	public Collection<? extends ProofNode<C>> getPremises() {
		return delegate_.getPremises();
	}

	@Override
	public String toString() {
		return delegate_.toString();
	}

}
