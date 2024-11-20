package q14;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GitlabProject extends GitlabPermissionsNode {
    private String name;
    private Map<User, PermissionsLevel> members = new HashMap<User, PermissionsLevel>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GitlabProject(String name, User owner) {
        this.name = name;
    }

    @Override
    public PermissionsLevel getUserPermissions(User user) {
        return members.get(user);
    }


    @Override
    public void updateUserPermissions(User userToUpdate, PermissionsLevel permissions, User updatingUser)
            throws GitlabAuthorisationException {
        authorise(updatingUser, PermissionsLevel.OWNER);
        members.put(userToUpdate, permissions);
    }


    public void runPipeline(Runnable runnable) {
        GitlabRunner runner = new GitlabRunner();
        runner.run(runnable);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "project");
        json.put("name", name);
        return json;
    }
}