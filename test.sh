#!/bin/sh
chmod +x gradlew;
./gradlew :data:testDebugUnitTest
./gradlew :app:testStagingDebugUnitTest
./gradlew :mobile:testDebugUnitTest
