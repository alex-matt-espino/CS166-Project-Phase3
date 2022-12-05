CREATE INDEX store_storeID
ON Store USING btree (storeID);

CREATE INDEX store_managerID
ON Store USING btree (managerID); 

CREATE INDEX user_userID
ON Users USING btree (userID);

CREATE INDEX user_userID
ON Users (userID);

CREATE INDEX product_storeID
ON Product USING btree (product_storeID);

CREATE INDEX product_productName
ON Product (storeID);

CREATE INDEX orders_productsName
ON Orders (productName);

CREATE INDEX orders_customerID
ON Orders USING btree (customerID);

CREATE INDEX orders_orderTime
ON Orders USING btree (orderTime);


CREATE INDEX productSupplyRequests_managerID
ON ProductSupplyRequests (managerID);

CREATE INDEX productSupplyRequests_productName
ON ProductSupplyRequests (managerID);

CREATE INDEX productUpdates_managerID
ON productUpdates (managerID);

CREATE INDEX productUpdates_productName
ON productUpdates (productName);

CREATE INDEX productUpdates_updateNumber
ON productUpdates USING btree (updateNumber);

CREATE INDEX productUpdates_updatedOn
ON productUpdates USING btree (updatedOn);