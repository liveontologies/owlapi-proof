package org.liveontologies.owlapi.proof;

import org.semanticweb.owlapi.model.OWLAxiom;

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

/**
 * An object from which derivations of an {@link OWLAxiom} can be accessed and
 * monitored for changes
 * 
 * @author Yevgeny Kazakov
 *
 */
public interface OWLProof {

	/**
	 * @return the node representing the last inference steps made to derive the
	 *         final conclusion; the derivation for premises used in these
	 *         inference steps can be unfolded recursively
	 */
	OWLProofNode getRoot();

	void addListener(ProofChangeListener listener);

	void removeListener(ProofChangeListener listener);

	void dispose();

}
