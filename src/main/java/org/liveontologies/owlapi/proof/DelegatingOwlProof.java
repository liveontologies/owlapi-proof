package org.liveontologies.owlapi.proof;

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

/**
 * An {@link OWLProof} view of some other non-null {@link OWLProof}. This
 * convenience class can be used for proof replacement (using
 * {@link #setDelegate(OWLProof)}) in which case all registered listeners
 * receive a notification about changes in the proof.
 * 
 * @author Yevgeny Kazakov
 */
public class DelegatingOwlProof implements OWLProof {

	private OWLProof delegate_;

	private final List<ProofChangeListener> listeners_ = new ArrayList<ProofChangeListener>();

	public DelegatingOwlProof(OWLProof delegate) {
		setDelegate(delegate);
	}

	public OWLProof getDelegate() {
		return delegate_;
	}

	public void setDelegate(OWLProof newDelegate) {
		if (newDelegate == null) {
			throw new NullPointerException();
		}
		// else
		if (newDelegate.equals(delegate_)) {
			// nothing changes
			return;
		}
		// else
		for (ProofChangeListener listener : listeners_) {
			delegate_.removeListener(listener);
			newDelegate.addListener(listener);
		}
		delegate_ = newDelegate;
		for (ProofChangeListener listener : listeners_) {
			listener.proofChanged();
		}
	}

	@Override
	public OWLProofNode getRoot() {
		return delegate_.getRoot();
	}

	@Override
	public void addListener(ProofChangeListener listener) {
		listeners_.add(listener);
		delegate_.addListener(listener);
	}

	@Override
	public void removeListener(ProofChangeListener listener) {
		listeners_.remove(listener);
		delegate_.removeListener(listener);
	}

	@Override
	public void dispose() {
		delegate_.dispose();
		listeners_.clear();
	}

}
