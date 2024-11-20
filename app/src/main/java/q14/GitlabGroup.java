package q14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class GitlabGroup extends GitlabPermissionsNode {
    private String name;
    private Map<User, PermissionsLevel> members = new HashMap<User, PermissionsLevel>();
    private List<GitlabPermissionsNode> subgroups = new ArrayList<GitlabPermissionsNode>();

    public GitlabGroup(String name, User creator) {
        this.name = name;
        members.put(creator, PermissionsLevel.OWNER);
    }

    public String getName() {
        return name;
    }

    public List<String> getUsersOfPermissionLevel(PermissionsLevel level) {
        return members.keySet().stream().filter(member -> members.get(member).equals(level)).map(member -> member.getName()).collect(Collectors.toList());
    }

    @Override
    public PermissionsLevel getUserPermissions(User user) {
        PermissionsLevel permission = members.get(user);
        if (permission != null) {
            return permission;
        }
        return null;
    }

    @Override
    public void updateUserPermissions(User userToUpdate, PermissionsLevel permissions, User updatingUser)
            throws GitlabAuthorisationException {
        authorise(updatingUser, PermissionsLevel.OWNER);
        PermissionsLevel existingPermission = getUserPermissions(userToUpdate);
        if (existingPermission != null && existingPermission.ordinal() <= permissions.ordinal()) {
        throw new GitlabAuthorisationException(
            "Cannot downgrade or override existing permissions through filtering authorisation");
        }
        if (!this.subgroups.isEmpty()) {
            this.subgroups.forEach(subgroup -> {
                try {
                    subgroup.updateUserPermissions(userToUpdate, permissions, updatingUser);
                } catch (GitlabAuthorisationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
        members.put(userToUpdate, permissions);

    }


    public GitlabGroup createSubgroup(String name, User user) throws GitlabAuthorisationException {
        authorise(user, PermissionsLevel.MAINTAINER);

        GitlabGroup group = new GitlabGroup(name, user);
        subgroups.add(group);
        return group;
    }


    public GitlabProject createProject(String name, User user) throws GitlabAuthorisationException {
        authorise(user, PermissionsLevel.DEVELOPER);

        GitlabProject project = new GitlabProject(name, user);
        subgroups.add(project);
        return project;
    }



    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", "group");
        json.put("name", name);

        JSONArray subgroupJSON = new JSONArray(
                subgroups.stream()
                        .map(GitlabPermissionsNode::toJSON)
                        .collect(Collectors.toList()));

        json.put("subgroups", subgroupJSON);

        return json;
    }
}