/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package register_attenance;

/**
 *
 * @author edgar
 */
public class Event{
    private String name, time_entry, time_start, time_end, time_limit, time_register;
    private int id, time_entry_int, time_start_int, time_end_int, time_limit_int, time_register_int;
    private boolean current_event;

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getTime_register() {
        return time_register;
    }

    public void setTime_register(String time_register) {
        this.time_register = time_register;
    }

    public int getTime_limit_int() {
        return time_limit_int;
    }

    public void setTime_limit_int(int time_limit_int) {
        this.time_limit_int = time_limit_int;
    }

    public int getTime_register_int() {
        return time_register_int;
    }

    public void setTime_register_int(int time_register_int) {
        this.time_register_int = time_register_int;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_entry() {
        return time_entry;
    }

    public void setTime_entry(String time_entry) {
        this.time_entry = time_entry;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public boolean isCurrent_event() {
        return current_event;
    }

    public void setCurrent_event(boolean current_event) {
        this.current_event = current_event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime_entry_int() {
        return time_entry_int;
    }

    public void setTime_entry_int(int time_entry_int) {
        this.time_entry_int = time_entry_int;
    }

    public int getTime_start_int() {
        return time_start_int;
    }

    public void setTime_start_int(int time_start_int) {
        this.time_start_int = time_start_int;
    }

    public int getTime_end_int() {
        return time_end_int;
    }

    public void setTime_end_int(int time_end_int) {
        this.time_end_int = time_end_int;
    }

    @Override
    public String toString() {
        return time_start + " - " + time_end + "   " + name;
    }
}
