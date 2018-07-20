package kejiban.entity;

import java.io.Serializable;

public class PageRequest implements Serializable {

    private int start;
    private int pageSize;

    public PageRequest(int start, int pageSize) {
        if (start <= 0) start = 1;
        this.start = start;
        this.pageSize = pageSize;
    }

    public int getStart() {
        return this.start;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public static PageRequest getPageRequest(String cmd, String startStr, String pageSizeStr) {
        int start;
        try {
            start = startStr == null ? 1 : Integer.parseInt(startStr);
        } catch (NumberFormatException e) {
            start = 1;
        }

        int pageSize;
        try {
            pageSize = pageSizeStr == null ? 10 : Integer.parseInt(pageSizeStr);
        } catch (NumberFormatException e) {
            pageSize = 10;
        }

        if (cmd == null) cmd = "current";

        if (pageSize < 10) pageSize = 10;
        if (pageSize > 100) pageSize = 100;

        if ("all".equals(cmd)) return new PageRequest(1, 2147483647);
        return new PageRequest(start, pageSize);
    }

}
