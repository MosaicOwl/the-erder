package ru.alastar.main;

public interface CommandExecutor
{
    String getDescription();
    void execute(String[] args);

}
