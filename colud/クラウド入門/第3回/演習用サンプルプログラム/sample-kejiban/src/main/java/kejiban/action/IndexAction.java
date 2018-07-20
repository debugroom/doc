/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package kejiban.action;

import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import kejiban.dao.ResDao;
import kejiban.entity.PageRequest;
import kejiban.entity.Res;
import kejiban.form.ResForm;
import kejiban.hibernate.Hibernate;

import org.hibernate.Transaction;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

public class IndexAction {

    public List<Res> reses;
    public List countRes;

    public Integer start;
    public Integer pageSize;

    @ActionForm
    @Resource
    protected ResForm resForm;

    @Execute(validator = false, urlPattern = "{start}-{cmd}")
    public String index() {
        ResDao dao = new ResDao();
        Transaction tx = Hibernate.openSession().beginTransaction();
        PageRequest pageRequest = PageRequest.getPageRequest(resForm.cmd, resForm.start, resForm.pageSize);
        reses = dao.getResesByPage(pageRequest);
        countRes = dao.getCountRes();
        start = pageRequest.getStart();
        pageSize = pageRequest.getPageSize();
        tx.commit();
        Hibernate.closeSession();
        return "index.jsp";
    }

    @Execute(input = "index.jsp")
    public String insert() {
        ResDao dao = new ResDao();
        Res entity = Beans.createAndCopy(Res.class, resForm).execute();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        entity.setDate(timestamp);

        Transaction tx = Hibernate.openSession().beginTransaction();
        dao.insertRes(entity);
        tx.commit();
        Hibernate.closeSession();
        return "?redirect=true";
    }
}
