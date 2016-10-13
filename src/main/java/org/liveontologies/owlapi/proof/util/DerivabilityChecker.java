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

/**
 * Testing derivability of conclusions using inferences. A conclusion is
 * derivable either if it is a conclusion of an inference with zero premises or
 * a conclusion of an inference all of which premises are derivable.
 * 
 * @author Yevgeny Kazakov
 *
 * @param <C>
 */
public interface DerivabilityChecker<C> {

	/**
	 * Checks if a given conclusion is derivable
	 * 
	 * @param conclusion
	 *            the conclusion to be tested for derivability
	 * @return {@code true} if conclusion is derivable and {@code false}
	 *         otherwise
	 */
	public boolean isDerivable(C conclusion);

}
