# Backend

## Тесты в CategoryTest

#### getCategoryPositiveTest
В ответ на запрос категории:  
* проверка кода успешного выполнения запроса;
* код категории всех- продуктов в ответе соответствует коду в запросе;
* название категории всех продуктов в ответе соответсвует коду в запросе;

#### GetCategoryNotFoodTest
В ответ на запрос категории:  
* проверка кода успешного выполнения запроса;
* код категории всех- продуктов в ответе соответствует коду в запросе;
* название категории  отличается от названия  категории с другим ID;
* в ответ на запрос категории с несуществующим ID сервер возвращает код 404 
   
## Тесты в ProductTest

### createProductInFoodCategoryTest
*  проверка успешного создания записи о продукте 

 ### void tearDown
 *  проверка успешного удаления записи о продукте 
 





