CREATE OR REPLACE FUNCTION insert_bank_account_info()
    RETURNS TRIGGER AS $$
DECLARE
    digits TEXT := '1234567890';
    letters TEXT := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    account_number TEXT;
BEGIN
    LOOP
        account_number := 'BY';
        FOR i IN 1..2 LOOP
                account_number := account_number || substr(digits, floor(random() * length(digits) + 1)::int, 1);
            END LOOP;
        FOR i IN 1..4 LOOP
                account_number := account_number || substr(letters, floor(random() * length(letters) + 1)::int, 1);
            END LOOP;
        FOR i IN 1..20 LOOP
                account_number := account_number || substr(digits, floor(random() * length(digits) + 1)::int, 1);
            END LOOP;
        IF NOT EXISTS (SELECT 1 FROM bank_account WHERE number = account_number) THEN
            EXIT;
        END IF;
    END LOOP;
    NEW.number := account_number;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_user_creation_id_trigger
    BEFORE insert ON bank_account
    FOR EACH ROW
    EXECUTE FUNCTION insert_bank_account_info();
