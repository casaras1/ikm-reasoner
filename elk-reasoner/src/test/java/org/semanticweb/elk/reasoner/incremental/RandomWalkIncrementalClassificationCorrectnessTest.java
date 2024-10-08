/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.reasoner.incremental;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.runner.RunWith;
import org.semanticweb.elk.loading.AllAxiomTrackingOntologyLoader;
import org.semanticweb.elk.loading.AxiomLoader;
import org.semanticweb.elk.loading.TestAxiomLoader;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.reasoner.SimpleManifestCreator;
import org.semanticweb.elk.reasoner.incremental.RandomWalkRunnerIO.ElkAPIBasedIO;
import org.semanticweb.elk.testing.ConfigurationUtils;
import org.semanticweb.elk.testing.ElkTestUtils;
import org.semanticweb.elk.testing.PolySuite.Config;
import org.semanticweb.elk.testing.PolySuite.Configuration;
import org.semanticweb.elk.testing.TestManifest;
import org.semanticweb.elk.testing.UrlTestInput;
import org.semanticweb.elk.testing4.PolySuite4;

/**
 * @author "Yevgeny Kazakov"
 * 
 */
@RunWith(PolySuite4.class)
public class RandomWalkIncrementalClassificationCorrectnessTest extends
		BaseRandomWalkIncrementalCorrectnessTest {

	public RandomWalkIncrementalClassificationCorrectnessTest(
			final TestManifest<UrlTestInput> testManifest) {
		super(testManifest);
	}

	@Override
	protected RandomWalkIncrementalClassificationRunner<ElkAxiom> getRandomWalkRunner(
			int rounds, int iterations) {
		return new RandomWalkIncrementalClassificationRunner<ElkAxiom>(rounds,
				iterations, new ElkAPIBasedIO());
	}

	@Override
	protected TestAxiomLoader getAxiomTrackingLoader(AxiomLoader fileLoader,
			OnOffVector<ElkAxiom> changingAxioms, List<ElkAxiom> staticAxioms) {
		// return new ClassAxiomTrackingOntologyLoader(fileLoader,
		// changingAxioms, staticAxioms);
		return new AllAxiomTrackingOntologyLoader(fileLoader, changingAxioms);
	}

	@Config
	public static Configuration getConfig()
			throws URISyntaxException, IOException {
		return ConfigurationUtils.loadFileBasedTestConfiguration(
				ElkTestUtils.TEST_INPUT_LOCATION,
				RandomWalkIncrementalClassificationCorrectnessTest.class,
				SimpleManifestCreator.INSTANCE, "owl");
	}

}
