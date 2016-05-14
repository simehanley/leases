UPDATE LEASE SET RESIDENTIAL = TRUE WHERE ID IN (SELECT LEASE_ID FROM lease_to_tenant_mapping WHERE TENANT_ID IN (SELECT ID FROM lease_tenant WHERE NAME LIKE 'Jacqueline Mary Lane'));

UPDATE LEASE_CATEGORY SET CATEGORY_COMPANY = 'JG&S JEWELLERY SUPERFUND PTY LTD' WHERE CATEGORY_NAME = 'JG&S JEWELLERY SUPER';
UPDATE LEASE_CATEGORY SET CATEGORY_ABN = 'ABN : 48 002 450 571' WHERE CATEGORY_NAME = 'JG&S JEWELLERY SUPER';
UPDATE LEASE_CATEGORY SET CATEGORY_COMPANY = 'INVESTRON PTY LTD' WHERE CATEGORY_NAME = 'INVESTRON';
UPDATE LEASE_CATEGORY SET CATEGORY_ABN = 'ABN : 25 104 920 792' WHERE CATEGORY_NAME = 'INVESTRON';
UPDATE LEASE_CATEGORY SET CATEGORY_COMPANY = 'MARIA GOUBRAN' WHERE CATEGORY_NAME = 'MARIA GOUBRAN';
UPDATE LEASE_CATEGORY SET CATEGORY_ABN = 'ABN : 17 169 693 870' WHERE CATEGORY_NAME = 'MARIA GOUBRAN';
UPDATE LEASE_CATEGORY SET CATEGORY_COMPANY = 'JOHN GOUBRAN & ASSOCIATES PTY LTD' WHERE CATEGORY_NAME = 'John Goubran & Associates';
UPDATE LEASE_CATEGORY SET CATEGORY_ABN = 'ABN : 80 070 974 819' WHERE CATEGORY_NAME = 'John Goubran & Associates';
UPDATE LEASE_CATEGORY SET CATEGORY_COMPANY = 'J.G.R.T HOLDINGS PTY LTD' WHERE CATEGORY_NAME = 'JGRT HOLDINGS';
UPDATE LEASE_CATEGORY SET CATEGORY_ABN = 'ABN : 64 098 577 523' WHERE CATEGORY_NAME = 'JGRT HOLDINGS';
UPDATE LEASE_CATEGORY SET CATEGORY_ADDRESS = 'UNIT 11/43 COLLEGE ST. GLADESVILLE  2111';
UPDATE LEASE_CATEGORY SET CATEGORY_PHONE = 'PH:  02 9879 0723  FX:  02 9879 0725';

UPDATE LEASE_PREMISES SET ADDRESS_LINE_ONE = '6-8 Herbert St', ADDRESS_LINE_TWO='ST LEONARDS NSW 2065' WHERE ADDRESS_LINE_ONE = '2A = 6-8 Herbert St';
UPDATE LEASE_PREMISES SET ADDRESS_LINE_TWO = 'GLADESVILLE NSW 2111' WHERE ADDRESS_LINE_ONE = '43-51 College St';
UPDATE LEASE_PREMISES SET ADDRESS_LINE_TWO = 'ST LEONARDS NSW 2065' WHERE ADDRESS_LINE_ONE = '2A Herbert St';
UPDATE LEASE_PREMISES SET ADDRESS_LINE_TWO = 'GLADESVILLE NSW 2111' WHERE ADDRESS_LINE_ONE = '46-48 Buffalo Rd';
UPDATE LEASE_PREMISES SET ADDRESS_LINE_TWO = 'NORTH RYDE NSW 2113' WHERE ADDRESS_LINE_ONE = '33 Waterloo Rd';
UPDATE LEASE_PREMISES SET ADDRESS_LINE_TWO = 'EPPING NSW 2121' WHERE ADDRESS_LINE_ONE = '2 Oxford St';

UPDATE LEASE_CATEGORY SET CATEGORY_ACCOUNT_NAME = 'J.G.R.T Holdings Pty Ltd', CATEGORY_BANK='St George Bank - Parramatta', CATEGORY_BSB='332 029', CATEGORY_ACCOUNT_NUMBER='5518 22959' WHERE CATEGORY_NAME = 'JGRT HOLDINGS';
UPDATE LEASE_CATEGORY SET CATEGORY_ACCOUNT_NAME = 'Maria Goubran', CATEGORY_BANK='St George Bank - Bankstown', CATEGORY_BSB='332 044', CATEGORY_ACCOUNT_NUMBER='5524 34552' WHERE CATEGORY_NAME = 'MARIA GOUBRAN';
UPDATE LEASE_CATEGORY SET CATEGORY_ACCOUNT_NAME = 'JG&S Jewellery Superfund Pty Ltd', CATEGORY_BANK='St George Bank - Bankstown', CATEGORY_BSB='332 044', CATEGORY_ACCOUNT_NUMBER='5510 67244' WHERE CATEGORY_NAME = 'JG&S JEWELLERY SUPER';
UPDATE LEASE_CATEGORY SET CATEGORY_ACCOUNT_NAME = 'Investron Pty Ltd', CATEGORY_BANK='St George Bank - Parramatta', CATEGORY_BSB='332 029', CATEGORY_ACCOUNT_NUMBER='5520 69399' WHERE CATEGORY_NAME = 'INVESTRON';
UPDATE LEASE_CATEGORY SET CATEGORY_ACCOUNT_NAME = 'John Goubran & Associates Pty Ltd', CATEGORY_BANK='St George Bank - Parramatta', CATEGORY_BSB='332 029', CATEGORY_ACCOUNT_NUMBER='5511 75237' WHERE CATEGORY_NAME = 'John Goubran & Associates';