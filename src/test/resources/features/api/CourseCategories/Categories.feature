Feature: As an administrator, I want to access course categories via an API connection.
  @API
  Scenario Outline: When a GET request is sent to the /api/categories endpoint with valid authorization,
  the response status code should be 200, the remark should be “success”, and the information of id(x)
  (slug, parent_id, icon, order, title, category_id, locale) should be validated.

    * The api user constructs the base url with the "admin" token.
    # Api kullanicisi "admin" token ile base urli olusturur
    * The api user sets "api/categories" path parameters.
    # Api kullanicisi "api/categories" path parametrelerini olusturur
    * The api user sends a GET request and saves the returned response.
    # Api kullanicisi GET request gonderir ve donen responsei kaydeder
    * The api user verifies that the status code is 200.
    # Api kullanicisi status codeun 200 oldugunu dogrular
    * The api user verifies that the "remark" information in the response body is "success".
    # Api kullanicisi response bodydeki remark bilgisinin "success" oldugunu dogrular
   # * The api user verifies the "<slug>", "<icon>", <order>, <id>, <category_id>, "<locale>" and "<title>" information of the item at <dataIndex> in the response body.
    # Api kullanıcısı response body icindeki <dataIndex> indexe sahip olanin "<slug>", "<icon>", <order>, <id>, <category_id>, "<locale>" ve "<title>" bilgilerini doğrular.

    Examples:
      | dataIndex | slug                    | icon                                                     | order| id | category_id | locale | title        |
      | 1         | En-guzel-ders-20 | /store/1/default_images/categories_icons/code.png|               213   | 604| 1161        | en     | En guzel ders|



