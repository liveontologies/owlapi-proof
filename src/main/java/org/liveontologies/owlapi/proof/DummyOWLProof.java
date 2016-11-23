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

import java.util.Collection;
import java.util.Collections;

import org.liveontologies.owlapi.proof.util.LeafProofNode;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Represents a trivial derivation for an {@link OWLAxiom} when this axiom is
 * derived by no inferences. This proof cannot change.
 * 
 * @author Yevgeny Kazakov
 */
public class DummyOWLProof extends LeafProofNode<OWLAxiom>
		implements OWLProofNode, OWLProof {

	public DummyOWLProof(OWLAxiom member) {
		super(member);
	}

	@Override
	public Collection<? extends OWLProofStep> getInferences() {
		return Collections.emptyList();
	}

	@Override
	public OWLProofNode getRoot() {
		return this;
	}

	@Override
	public void addListener(ProofChangeListener listener) {
		// proof cannot change

	}

	@Override
	public void removeListener(ProofChangeListener listener) {
		// proof cannot change
	}

	@Override
	public void dispose() {
		// no-op
	}

}