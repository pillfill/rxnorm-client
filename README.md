#PillFill RxNorm Client

[![Build Status](https://travis-ci.org/pillfill/rxnorm-client.svg?branch=master)](https://travis-ci.org/pillfill/rxnorm-client)

 This project is working to create a lightweight REST Java/Android client library for the [RxNav APIs](http://rxnav.nlm.nih.gov/APIsOverview.html), services managed by the National Library of Medicine (NLM) &  National Institutes of Health (NIH). While we're unabashed fans of the services, please note this project **has no affiliation with NLM, NIH, or HHS**.

##Why?

 The RxNav API suite is a great drug information resource. It'd be a wonderful tool for many mHealth apps.

 The method endorsed by RxNav to [build WSDL client stubs](http://rxnav.nlm.nih.gov/RxNormAPIMakeApp.html) for their SOAP interface, however, is painful and doesn't work well for most mobile platforms. Several years ago we tried to force a WSDL client onto Android anyway, eventually abandoning the idea after plenty of frustration. It now appears that [we were in good company](https://stackoverflow.com/questions/5461127/using-jaxb-with-google-android).

 As far as we're aware, there is no separate schema for the service objects since [everything is baked into WSDL definitions](http://rxnav.nlm.nih.gov/RxNormDBService.xml). There are [database schema/RRF files](http://www.nlm.nih.gov/research/umls/rxnorm/docs/rxnormfiles.html) available, but aren't terribly helpful since the service bindings we'd need interested in all appear to be defined in code and Java→WSDL generated.

##Strategy

 This project was started to (slowly) build an RxNorm REST client with development broken it into two separate efforts:

 1. Define Separate Data Schemas

 Our current approach is to transcribe the available service information from the WSDL definition and REST service responses into JSON Schema documents. [These schema documents](https://github.com/pillfill/rxnorm-client/tree/master/src/main/resources/schemas) are then used to generate Java POJO objects via [jsonschema2pojo](https://github.com/joelittlejohn/jsonschema2pojo/). We're taking this databinding approach with the intention to make the JSON Schemas reusable across other languages and platforms (though this library will remain Android/Java focused).

 2. Create (Simple) Service Proxies

 We also plan to build some lightweight service proxies modeled after the RxNav service documentation. The mapping between APIs and service objects isn't obvious, so the service proxies should reduce some of that confusion.


##Goals

Nothing too ambitious, but we'll try to stick to a few principles along the way:

 * Minimize external runtime dependencies- Currently only the Gson library.
 * Keep the library focused on the data schemas and basic service proxies.
 * Unit test using JSON documents captured from the live RxNav service.
 * Integration test using the real RxNav service endpoints (lightly, though, [as to not violate RxNav's ToS](http://rxnav.nlm.nih.gov/TermOfService.html)).
 * Depend on schemas to generate databinding objects in order to maximize reusability.
 * Try to provide a clear mapping to the [RxNav service documentation](http://rxnav.nlm.nih.gov/APIsOverview.html).


##Current Status

 We have a subset of JSON schemas defined and unit tested for the [RxNorm, NDFRT, and Interaction](https://github.com/pillfill/rxnorm-client/tree/master/src/main/resources/schemas) services against static JSON content.

###Implemented Service Proxies:

 * NdfrtService ([NIH Documentation](http://rxnav.nlm.nih.gov/NdfrtAPIs.html))
  * Service Interface: [NdfrtService](https://github.com/pillfill/rxnorm-client/blob/master/src/main/java/com/apothesource/pillfill/rxnorm/service/ndfrt/NdfrtService.java)
  * Default Implementation: [NihNdfrtServiceProxy](https://github.com/pillfill/rxnorm-client/blob/master/src/main/java/com/apothesource/pillfill/rxnorm/service/ndfrt/NihNdfrtServiceProxy.java)
  * Integration Tests: [NihNdfrtServiceProxy](https://github.com/pillfill/rxnorm-client/blob/master/src/test/java/com/apothesource/pillfill/rxnorm/service/ndfrt/NihNdfrtServiceProxyTest.java)

 * RxNormService ([NIH Documentation](http://rxnav.nlm.nih.gov/RxNormAPIs.html))
  * Service Interface: [RxNormService](https://github.com/pillfill/rxnorm-client/blob/master/src/main/java/com/apothesource/pillfill/rxnorm/service/RxNormService.java)
  * Default Implementation: [NihRxNormServiceProxy](https://github.com/pillfill/rxnorm-client/blob/master/src/main/java/com/apothesource/pillfill/rxnorm/service/NihRxNormServiceProxy.java)
  * Integration Tests: [NihRxNormServiceProxyTests](https://github.com/pillfill/rxnorm-client/blob/master/src/test/java/com/apothesource/pillfill/rxnorm/service/NihRxNormServiceProxyTest.java)

 * InteractionService ([NIH Documentation](http://rxnav.nlm.nih.gov/InteractionAPIs.html))
  * Service Interface: [InteractionService](https://github.com/pillfill/rxnorm-client/blob/master/src/main/java/com/apothesource/pillfill/rxnorm/service/interaction/InteractionService.java)
  * Default Implementation: [NihInteractionServiceProxy](https://github.com/pillfill/rxnorm-client/blob/master/src/main/java/com/apothesource/pillfill/rxnorm/service/interaction/NihInteractionServiceProxy.java)
  * Integration Tests: [NihRxNormServiceProxyTests](https://github.com/pillfill/rxnorm-client/blob/master/src/test/java/com/apothesource/pillfill/rxnorm/service/interaction/NihInteractionServiceProxyTest.java)


Want to contribute? Pull requests are encouraged!


##Building the RxNorm Library

 The project is gradle based:

 `./gradlew clean build`

 This will cause the datamodel objects to be purged and regenerated in `src/gen/java`, also rerunning all unit tests. It should build a jar file in the `build/libs` directory, making integration with other projects easier. Note that many of the schemas are still being created and/or optimized and you should **expect frequent breaking changes for now**. We'll create a stable branch and release to maven central once we get most of the basic service proxies created and tested. You can also run the integration tests like so:

 `./gradlew clean build integrationTest`

##Using the RxNorm Library

 Most users will want to just use one of the services. You can do so like this:

 ```Java
  NdfrtService serviceProxy = new NdfrtServiceProxy();
  FullConceptResponse response = serviceProxy.getAllInfo("N0000146307");

  //Begin interrogation of the SULINDAC drug concept
 ```

 See the [NIH NdfrtService proxy integration tests](https://github.com/pillfill/rxnorm-client/blob/master/src/test/java/com/apothesource/pillfill/rxnorm/service/ndfrt/NihNdfrtServiceProxyTest.java)
 for more examples.

##Disclaimer

 This product uses publicly available data from the U.S. National Library of Medicine (NLM), National Institutes of Health, Department of Health and Human Services; NLM is not responsible for the product and does not endorse or recommend this or any other product.

##License

 The MIT License (MIT)

 Copyright (c) 2015, Apothesource, Inc.

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
