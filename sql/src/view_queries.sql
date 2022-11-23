-- view stores

-- Browse Products 
SELECT p.storeID, p.productName, p.numberOfUnits
FROM product p, store s
WHERE p.storeID = s.storeID AND s.storeID = 1;
-- view recent orders

-- view recent updates


SELECT p.updateNumber, p.updatedOn
FROM productUpdates p
LEFT OUTER JOIN productUpdates p1
ON p.updateNumber = p1.updateNumber
AND p.updatedOn < p1.updatedOn
WHERE p1.updateNumber IS NULL
ORDER BY p.updatedOn asc
FETCH FIRST 10 ROWS ONLY;

-- view popular products

-- view popular customers

