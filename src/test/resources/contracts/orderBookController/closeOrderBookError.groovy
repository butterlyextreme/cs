import org.springframework.cloud.contract.spec.Contract
Contract.make {
  description ""
  request{
    method PUT()
    url("/orderbook/f4944dab-2846-44d5-891a-0ba3a78fb1ce") {
    }
    headers {
      header 'Content-Type': 'application/json'
    }
  }
  response {
    body("Generic Internal Error")
    status 500
  }
}
