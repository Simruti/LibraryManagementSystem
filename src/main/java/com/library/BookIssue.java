package com.library;

import java.time.LocalDate;
import java.util.Date;

public class BookIssue {
    int isbn;
    int member_id;
    Date issue_date;
    Date return_date;

    public BookIssue(int isbn, int member_id) {
        this.isbn = isbn;
        this.member_id = member_id;
    }

    public int getIsbn() {
        return isbn;
    }

    public int getMember_id() {
        return member_id;
    }

    public Date getIssue_date()
    {
        return issue_date;
    }

    public Date getReturn_date() {
        return return_date;
    }


}
