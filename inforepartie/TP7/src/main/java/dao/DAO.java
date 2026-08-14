package dao;

import java.util.ArrayList;
import java.util.List;

public class DAO {

    List<String> st = new ArrayList<>();

    public DAO() {
        st.add("Pommes");
        st.add("Poires");
        st.add("Bananes");
        st.add("Fraises");
        st.add("Cerises");
    }

    public List<String> findAll() {
        return st;
    }

    public String findById(int id) {
        return st.get(id % st.size());
    }
}