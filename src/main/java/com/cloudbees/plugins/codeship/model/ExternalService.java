package com.cloudbees.plugins.codeship.model;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Translated from https://github.com/codeship/jet/blob/master/service/external_services.go#ExternalService
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class ExternalService {

    public boolean add_docker;
    public ExternalBuild build;
    public boolean cached;
    public List<String> cap_add;
    public List<String> cap_drop;
    public String command;
    public String cpuset;
    public long spu_shares;
    public String default_cache_branch;
    public List<String> dns;
    public List<String> dns_search;
    public String dockercfg_service;
    public String dockerfile;
    public String domainname;
    public String encrypted_dockercfg_path;
    public List<String> encrypted_env_file;
    public List<String>encrypted_environment;
    public List<String> entrypoint;
    public List<String> env_file;
    public List<String> environment;
    public List<String> expose;
    public List<String> extra_hosts;
    public String hostname;
    public String image;
    public List<String> links;
    public List<String> depends_on;
    public String mem_limit;
    public boolean privileged;
    public List<String> ports;
    public boolean read_only;
    public String restart;
    public List<String> security_opt;
    public String user;
    public List<String> wolumes;
    public List<String> volumes_from;
    public String working_dir;
    
    public void setAdd_docker(boolean add_docker) {
        this.add_docker = add_docker;
    }

    public void setBuild(ExternalBuild build) {
        this.build = build;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public void setCap_add(List<String> cap_add) {
        this.cap_add = cap_add;
    }

    public void setCap_drop(List<String> cap_drop) {
        this.cap_drop = cap_drop;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCpuset(String cpuset) {
        this.cpuset = cpuset;
    }

    public void setSpu_shares(long spu_shares) {
        this.spu_shares = spu_shares;
    }

    public void setDefault_cache_branch(String default_cache_branch) {
        this.default_cache_branch = default_cache_branch;
    }

    public void setDns(List<String> dns) {
        this.dns = dns;
    }

    public void setDns_search(List<String> dns_search) {
        this.dns_search = dns_search;
    }

    public void setDockercfg_service(String dockercfg_service) {
        this.dockercfg_service = dockercfg_service;
    }

    public void setDockerfile(String dockerfile) {
        this.dockerfile = dockerfile;
    }

    public void setDomainname(String domainname) {
        this.domainname = domainname;
    }

    public void setEncrypted_dockercfg_path(String encrypted_dockercfg_path) {
        this.encrypted_dockercfg_path = encrypted_dockercfg_path;
    }

    public void setEncrypted_env_file(List<String> encrypted_env_file) {
        this.encrypted_env_file = encrypted_env_file;
    }

    public void setEncrypted_environment(List<String> encrypted_environment) {
        this.encrypted_environment = encrypted_environment;
    }

    public void setEntrypoint(List<String> entrypoint) {
        this.entrypoint = entrypoint;
    }

    public void setEnv_file(List<String> env_file) {
        this.env_file = env_file;
    }

    public void setEnvironment(List<String> environment) {
        this.environment = environment;
    }

    public void setExpose(List<String> expose) {
        this.expose = expose;
    }

    public void setExtra_hosts(List<String> extra_hosts) {
        this.extra_hosts = extra_hosts;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public void setDepends_on(List<String> depends_on) {
        this.depends_on = depends_on;
    }

    public void setMem_limit(String mem_limit) {
        this.mem_limit = mem_limit;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public void setRead_only(boolean read_only) {
        this.read_only = read_only;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public void setSecurity_opt(List<String> security_opt) {
        this.security_opt = security_opt;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setWolumes(List<String> wolumes) {
        this.wolumes = wolumes;
    }

    public void setVolumes_from(List<String> volumes_from) {
        this.volumes_from = volumes_from;
    }

    public void setWorking_dir(String working_dir) {
        this.working_dir = working_dir;
    }

    public String getImageForPipeline() {
        if (build == null) {
            return "docker.image('" + this.image + "')";
        } else {
            return "docker.build(image: '" + this.build.image + "')"; // TODO add support for build parameters
        }

    }
}
