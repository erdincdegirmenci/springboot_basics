INSERT INTO Audits (id, type, description) VALUES
(1, 'LOGIN', 'User logged in successfully'),
(2, 'LOGIN_FAILURE', 'User login attempt failed'),
(3, 'LOGIN_SUCCESS', 'User logged in successfully'),
(4, 'PROFILE_UPDATE', 'User updated profile details'),
(5, 'PROFILE_PICTURE_UPDATE', 'User updated profile picture'),
(6, 'ROLE_UPDATE', 'User role updated'),
(7, 'ACCOUNT_SETTINGS_UPDATE', 'User updated account settings'),
(8, 'PASSWORD_UPDATE', 'User updated password'),
(9, 'MFA_UPDATE', 'User updated multi-factor authentication settings');

INSERT INTO Roles (id, name, description) VALUES
(1, 'ROLE_USER', 'Basic user with limited permissions'),
(2, 'ROLE_MANAGER', 'User with management permissions'),
(3, 'ROLE_ADMIN', 'Administrator with higher-level permissions'),
(4, 'ROLE_SYSADMIN', 'System administrator with full access to all functionalities');

INSERT INTO Permissions (id, name, description) VALUES
(1, 'READ:USER', 'Permission to read user data'),
(2, 'UPDATE:USER', 'Permission to update user data'),
(3, 'CREATE:USER', 'Permission to create new user accounts'),
(4, 'DELETE:USER', 'Permission to delete user accounts');

INSERT INTO RolePermissions (id, roleid, permissionid) VALUES
-- ROLE_USER => READ:USER
(1, 1, 1),  -- ROLE_USER (id=1) için READ:USER (id=1)

-- ROLE_ADMIN => READ:USER, UPDATE:USER
(2, 3, 1),  -- ROLE_ADMIN (id=3) için READ:USER (id=1)
(3, 3, 2),  -- ROLE_ADMIN (id=3) için UPDATE:USER (id=2)

-- ROLE_MANAGER => READ:USER, UPDATE:USER, CREATE:USER
(4, 2, 1),  -- ROLE_MANAGER (id=2) için READ:USER (id=1)
(5, 2, 2),  -- ROLE_MANAGER (id=2) için UPDATE:USER (id=2)
(6, 2, 3),  -- ROLE_MANAGER (id=2) için CREATE:USER (id=3)

-- ROLE_SYSADMIN => READ:USER, UPDATE:USER, CREATE:USER, DELETE:USER
(7, 4, 1),  -- ROLE_SYSADMIN (id=4) için READ:USER (id=1)
(8, 4, 2),  -- ROLE_SYSADMIN (id=4) için UPDATE:USER (id=2)
(9, 4, 3),  -- ROLE_SYSADMIN (id=4) için CREATE:USER (id=3)
(10, 4, 4); -- ROLE_SYSADMIN (id=4) için DELETE:USER (id=4)