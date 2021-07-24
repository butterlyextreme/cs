import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description ""
    request{
        method PUT()
        url("/orderbook/e4944dab-2846-44d5-891a-0ba3a78fb1cc") {
        }
        headers {
            header 'Content-Type': 'application/json'
        }
    }
    response {
        body("")
        status 200
    }
}
