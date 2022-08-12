package com.dew

import com.dew.analytics.application.create.CreateAnalyticsCommand
import com.dew.common.domain.bus.Mediator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

@MicronautTest
@Testcontainers
class AnalyticsSpec extends Specification implements TestPropertyProvider {

    static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017)

    @Inject
    Mediator mediator

    @Inject
    AnalyticsClient client

    def "interact with analytics api"() {
        given:
        mediator.send(new CreateAnalyticsCommand(20, 200000, "users")).blockFirst()

        when:
        var response = client.find("users")

        then:
        response.body.present
        response.body().products == 20
    }

    @Override
    Map<String, String> getProperties() {
        mongo.start()

        return ["mongodb.uri": mongo.replicaSetUrl]
    }

}
