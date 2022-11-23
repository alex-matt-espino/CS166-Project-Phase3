-- view stores
-- SELECT S.storeID, S.name
-- FROM Store S
-- WHERE
--pass in user lat/long,

-- view products *
SELECT *
FROM product;

-- view recent orders *
SELECT O.storeID, S.name, O.productName, O.unitsOrdered, O.orderTime
FROM Orders O, Store S, Users U
WHERE O.storeID = S.storeID AND
U.userID = O.customerID AND
U.userID = '32' --in Retail.java, pass in stored userID (store after login)
GROUP BY O.storeID, S.name, O.productName, O.unitsOrdered, O.orderTime
ORDER BY 5 DESC
LIMIT 5;

-- view recent updates *
SELECT p.updateNumber, p.updatedOn
FROM productUpdates p
LEFT OUTER JOIN productUpdates p1
ON p.updateNumber = p1.updateNumber
AND p.updatedOn < p1.updatedOn
WHERE p1.updateNumber IS NULL
ORDER BY p.updatedOn asc
FETCH FIRST 10 ROWS ONLY;


-- view popular products *
SELECT *
FROM (SELECT P.productName, COUNT(O.productName) AS total_orders
      FROM Product P, Orders O, Users U, Store S
      WHERE U.userID = S.managerID AND --check if user is manager
      P.storeID = S.storeID AND
      O.storeID = S.storeID AND
      P.productName = O.productName AND
      S.managerID = '40' --in Retail.java, pass in stored userID (store after login, check if manager)
      GROUP BY P.productName) AS popular_products
ORDER BY 2 DESC
LIMIT 5;

--test
-- SELECT DISTINCT O.productName, COUNT(O.productName)
-- FROM Orders O, Store S, Users U
-- WHERE S.managerID = '25' AND
-- S.storeID = O.storeID
-- GROUP BY O.productName
-- ORDER BY 2 DESC
-- LIMIT 10;

-- view popular customers
