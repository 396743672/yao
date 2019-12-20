package com.y3tu.yao.upms.configure;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.util.StrUtil;

/**
 * 自定义 p6spy sql输出格式
 *
 * @author y3tu
 */
public class P6spySqlFormatConfigure implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StrUtil.isNotBlank(sql) ? DateUtil.now()
                + " | 耗时 " + elapsed + " ms | SQL 语句：" + StrUtil.LF + sql.replaceAll("[\\s]+", StrUtil.SPACE) + ";" : StrUtil.EMPTY;
    }
}
