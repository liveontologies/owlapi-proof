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

public class DelegatingProofNode<C> implements ProofNode<C> {

	private final ProofNode<C> delegate_;

	protected DelegatingProofNode(ProofNode<C> delegate) {
		this.delegate_ = delegate;
	}

	protected ProofNode<C> getDelegate() {
		return delegate_;
	}

	@Override
	public C getMember() {
		return delegate_.getMember();
	}

	@Override
	public Collection<? extends ProofStep<C>> getInferences() {
		return delegate_.getInferences();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof DelegatingProofNode<?>) {
			return delegate_.equals(((DelegatingProofNode<?>) o).delegate_);
		}
		// else
		return false;
	}

	@Override
	public int hashCode() {
		return delegate_.hashCode();
	}

	@Override
	public String toString() {
		return delegate_.toString();
	}

}
