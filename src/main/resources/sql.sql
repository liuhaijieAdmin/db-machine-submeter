-- ---------创建第一张表的SQL语句----------
CREATE TABLE `month_bills_202211`  (
  `month_bills_id` int(8) NOT NULL AUTO_INCREMENT COMMENT '账单ID',
  `serial_number` varchar(50) NOT NULL COMMENT '流水号',
  `bills_info` text NOT NULL COMMENT '账单详情',
  `pay_money` decimal(10,3) NOT NULL COMMENT '支付金额',
	`machine_serial_no` varchar(20) NOT NULL COMMENT '收银机器',
  `bill_date` timestamp NOT NULL COMMENT '账单日期',
	`bill_comment` varchar(100) NULL DEFAULT '无' COMMENT '账单备注',
	PRIMARY KEY (`month_bills_id`) USING BTREE,
	UNIQUE `serial_number` (`serial_number`),
	KEY `bill_date` (`bill_date`)
)
ENGINE = InnoDB 
CHARACTER SET = utf8 
COLLATE = utf8_general_ci 
ROW_FORMAT = Compact;

insert into month_bills_202211(serial_number,bills_info,pay_money,machine_serial_no) values
("NF-001-X1668493829688","黄金竹子*2：8888.888",8888.888,"NF-001-X");


select * from month_bills_202211;

-- ------动态创建表的存储过程--------
DELIMITER // 
DROP PROCEDURE IF EXISTS create_table_by_month //
CREATE PROCEDURE `create_table_by_month`()
BEGIN
    -- 用于记录下一个月份是多久
    DECLARE nextMonth varchar(20);
    -- 用于记录创建表的SQL语句
    DECLARE createTableSQL varchar(5210);
    -- 执行创建表的SQL语句后，获取表的数量
    DECLARE tableCount int;
    -- 用于记录要生成的表名
    DECLARE tableName varchar(20);
    -- 用于记录表的前缀
    DECLARE table_prefix varchar(20);

  -- 获取下个月的日期并赋值给nextMonth变量
  SELECT SUBSTR(
    replace(
        DATE_ADD(CURDATE(), INTERVAL 1 MONTH),
    '-', ''),
  1, 6) INTO @nextMonth;

  -- 设置表前缀变量值为td_user_banks_log_
  set @table_prefix = 'month_bills_';

  -- 定义表的名称=表前缀+月份，即 month_bills_2022112 这个格式
  SET @tableName = CONCAT(@table_prefix, @nextMonth);
  -- 定义创建表的SQL语句
  set @createTableSQL=concat("create table if not exists ",@tableName,"(
				`month_bills_id` int(8) NOT NULL AUTO_INCREMENT COMMENT '账单ID',
				`serial_number` varchar(50) NOT NULL COMMENT '流水号',
				`bills_info` text NOT NULL COMMENT '账单详情',
				`pay_money` decimal(10,3) NOT NULL COMMENT '支付金额',
				`machine_serial_no` varchar(20) NOT NULL COMMENT '收银机器',
				`bill_date` timestamp NOT NULL DEFAULT now() COMMENT '账单日期',
				`bill_comment` varchar(100) NULL DEFAULT '无' COMMENT '账单备注',
			PRIMARY KEY (`month_bills_id`) USING BTREE,
            UNIQUE `serial_number` (`serial_number`),
            KEY `bill_date` (`bill_date`)
			) ENGINE = InnoDB 
			CHARACTER SET = utf8 
			COLLATE = utf8_general_ci 
			ROW_FORMAT = Compact;");
  
  -- 使用 PREPARE 关键字来创建一个预备执行的SQL体
  PREPARE create_stmt from @createTableSQL; 
  -- 使用 EXECUTE 关键字来执行上面的预备SQL体：create_stmt
  EXECUTE create_stmt;
  -- 释放掉前面创建的SQL体（减少内存占用）
  DEALLOCATE PREPARE create_stmt;

  -- 执行完建表语句后，查询表数量并保存再 tableCount 变量中
  SELECT 
    COUNT(1) INTO @tableCount 
  FROM 
    information_schema.`TABLES` 
  WHERE TABLE_NAME = @tableName;
  
  -- 查询一下对应的表是否已存在
  SELECT @tableCount 'tableCount';

END //
delimiter ;



-- ------------创建定时器------------------
create EVENT -- 创建一个定时器
    `create_table_by_month_event` 
ON SCHEDULE EVERY -- 每间隔一个月执行一次
    1 MONTH
STARTS -- 从2022-11-28 00:00:00后开始
    '2022-11-28 00:00:00' 
ON COMPLETION -- 执行完成之后不删除定时器
    PRESERVE ENABLE  
DO -- 每次触发定时器时执行的语句
    call create_table_by_month();


show variables like 'event_scheduler';
set global event_scheduler = 1;