package org.example.model;

//а в лабе это объясняли!
public class ModelUser
{
    //
    private String idUser;
    private String nameUser;
    private String firstnameUser;
    private String patronymic;
    private String roleUser;
    private String status;
    private String post;
    private short err_auth = 0;
    //

    //конструкторы
    public ModelUser(String id)
    {
        idUser = id;
        nameUser = "";
        firstnameUser = "";
        patronymic = "";
        roleUser = "";
        status = "";
        post = "";
    }
    public ModelUser(String nameUser, String firstnameUser, String patronymic, String roleUser)
    {
        idUser = "";
        this.nameUser = nameUser;
        this.firstnameUser = firstnameUser;
        this.patronymic = patronymic;
        this.roleUser = roleUser;
        status = "";
        post = "";
    }
    public ModelUser(String idUser, String nameUser, String firstnameUser, String patronymic, String roleUser, String status, String post)
    {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.firstnameUser = firstnameUser;
        this.patronymic = patronymic;
        this.roleUser = roleUser;
        this.status = status;
        this.post = post;
    }
    //конструкторы

    //set
    public void setIdUser( String idUser)
    {
        this.idUser = idUser;
    }
    public void setNameUser (String nameUser)
    {
        this.nameUser = nameUser;
    }
    public void setFirstnameUser(String firstnameUser)
    {
        this.firstnameUser = firstnameUser;
    }
    public void setPatronymic(String patronymic)
    {
        this.patronymic = patronymic;
    }
    public void setRoleUser (String roleUser)
    {
        this.roleUser = roleUser;
    }
    public void setStatus (String status)
    {
        this.status = status;
    }
    public void setPost (String post)
    {
        this.post = post;
    }
    //set

    //get
    public short getErr_auth ()
    {
        return err_auth;
    }
    public String getIdUser ()
    {
        return idUser;
    }
    public String getNameUser()
    {
        return nameUser;
    }
    public String getFirstnameUser()
    {
        return firstnameUser;
    }
    public String getRoleUser()
    {
        return roleUser;
    }
    public String getPatronymic ()
    {
        return patronymic;
    }
    public char getN()
    {
        return nameUser.charAt(0);
    }
    public char getP()
    {
        return patronymic.charAt(0);
    }
    public String getPost ()
    {
        return post;
    }
    public String getStatus ()
    {
        return status;
    }
    public void getData ()
    {
        System.out.println("Id: " + getIdUser() + "\nname: " + getNameUser() + "\nlast name: " + getFirstnameUser() + "\n" +
                "\nRole: " + getRoleUser() + "\nPost: " + getPost() + "\nStatus: " + getStatus());
    }
    //get
    public void add_err ()
    {
        err_auth++;
    }
    public void reset_err ()
    {
        err_auth = 0;
    }
}
