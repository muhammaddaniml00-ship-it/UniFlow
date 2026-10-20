package com.student.uniflow.model;

public class UserProfile {
    private String name;
    private String nim;
    private String program;

    public UserProfile() {}

    public UserProfile(String name, String nim, String program) {
        this.name = name;
        this.nim = nim;
        this.program = program;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
}
