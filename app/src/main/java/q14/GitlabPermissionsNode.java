package q14;

import org.json.JSONObject;

public abstract class GitlabPermissionsNode {
    public abstract String getName();

    public abstract PermissionsLevel getUserPermissions(User user);

    public abstract void updateUserPermissions(User userToUpdate, PermissionsLevel permissions, User userUpdating)
            throws GitlabAuthorisationException;

    public GitlabGroup createSubgroup(String name, User creator) throws GitlabAuthorisationException {
        return null;
    }

    public  GitlabProject createProject(String name, User user) throws GitlabAuthorisationException {
        return null;
    }

    public void authorise(User user, PermissionsLevel requiredPermissionsLevel) throws GitlabAuthorisationException {
        int perms = getUserPermissions(user).ordinal();
        int requiredPerms = requiredPermissionsLevel.ordinal();
        if (perms > requiredPerms) {
            throw new GitlabAuthorisationException("User is not authorised");
        }
    }

    public abstract JSONObject toJSON();
}