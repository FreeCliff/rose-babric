package me.ht9.rose.feature.gui.proxy;

import java.net.Proxy;

public class ProxyData
{
    private String name;
    private String ip;
    private String auth;
    private Proxy.Type type;

    public ProxyData(String name, String ip, String auth, Proxy.Type type)
    {
        this.name = name;
        this.ip = ip;
        this.auth = auth;
        this.type = type;
    }

    public String name()
    {
        return name;
    }

    public String ip()
    {
        return ip;
    }

    public String auth()
    {
        return auth;
    }

    public Proxy.Type type() {
        return type;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public void setAuth(String auth)
    {
        this.auth = auth;
    }

    public void setType(Proxy.Type type)
    {
        this.type = type;
    }
}
