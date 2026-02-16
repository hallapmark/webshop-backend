-- 1. Rename old integer role column
ALTER TABLE person
    RENAME COLUMN role TO role_int;

-- 2. Add new role column as string
ALTER TABLE person
    ADD COLUMN role VARCHAR(50);

-- 3. Convert old integer values to enum strings
UPDATE person
SET role = CASE role_int
               WHEN 0 THEN 'CUSTOMER'
               WHEN 1 THEN 'ADMIN'
               WHEN 2 THEN 'SUPERADMIN'
    END;

-- 4. Enforce not null
ALTER TABLE person
    ALTER COLUMN role SET NOT NULL;

-- 5. Drop old column after verification
ALTER TABLE person DROP COLUMN role_int;
