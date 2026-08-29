ALTER TABLE product ADD COLUMN distribution_center VARCHAR(2);

WITH numbered AS (
    SELECT id, ROW_NUMBER() OVER (ORDER BY id) AS rn
    FROM product
)
UPDATE product
SET distribution_center = CASE MOD(numbered.rn, 3)
    WHEN 0 THEN 'RJ'
    WHEN 1 THEN 'MG'
    ELSE 'SP'
END
FROM numbered
WHERE product.id = numbered.id;

ALTER TABLE product ALTER COLUMN distribution_center SET NOT NULL;
