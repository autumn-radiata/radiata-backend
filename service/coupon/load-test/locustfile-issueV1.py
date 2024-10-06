import random
from locust import task, FastHttpUser

class HelloWorld(FastHttpUser):
  connection_timeout = 10.0
  network_timeout = 10.0

  @task
  def issue(self):
      couponId = 1
      headers = {
        "X-UserId" : random.randint(1, 10000000)
      }

      with self.rest("POST", f"/coupons/{couponId}/issue", headers=headers):
        pass
