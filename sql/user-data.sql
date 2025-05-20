-- 用户
INSERT INTO user (id, name, email, password, mobile, created_at, updated_at) VALUES (1898625795887337472, null, null, '123456', '18123456789', null, null);
INSERT INTO user (id, name, email, password, mobile, created_at, updated_at) VALUES (1898625795887337472, null, null, null, '18012345678', null, null);

-- 乘车人
INSERT INTO passenger (id, user_id, name, id_card, mobile, type, created_at, updated_at) VALUES (1913801127279857664, 1898625795887337472, '李四', '987654321987654321', '18987654321', '1', '2025-04-20 11:45:20.099', '2025-04-20 11:45:20.099');
INSERT INTO passenger (id, user_id, name, id_card, mobile, type, created_at, updated_at) VALUES (1913877759806017536, 1898625795887337472, '张山', '456456456456456456', '18456456456', '3', '2025-04-20 16:49:50.717', '2025-04-20 16:49:50.717');
INSERT INTO passenger (id, user_id, name, id_card, mobile, type, created_at, updated_at) VALUES (1915957156415410176, 1898625795887337472, '王五', '456456456456456456', '18456465456', '1', '2025-04-26 10:32:37.502', '2025-04-26 10:32:37.502');
INSERT INTO passenger (id, user_id, name, id_card, mobile, type, created_at, updated_at) VALUES (1920836723038883840, 1901617288252100608, '六六', '123456789123456789', '18012345678', '1', '2025-05-09 21:42:16.842', '2025-05-09 21:42:16.842');