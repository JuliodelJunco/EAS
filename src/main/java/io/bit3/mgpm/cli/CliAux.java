package io.bit3.mgpm.cli;

import java.util.*;

public class CliAux {
    private Set<String> remoteBranches = new TreeSet<>();
    private Map<String, List<String>> remoteBranchesUsedAsUpstream = new HashMap<>();
    private String remoteName;
    private List<String> addedBranchNames = new ArrayList<>();
    private List<String> deletedBranchNames = new ArrayList<>();

    public Set<String> getRemoteBranches() {
        return remoteBranches;
    }

    public void setRemoteBranches(Set<String> remoteBranches) {
        this.remoteBranches = remoteBranches;
    }

    public Map<String, List<String>> getRemoteBranchesUsedAsUpstream() {
        return remoteBranchesUsedAsUpstream;
    }

    public void setRemoteBranchesUsedAsUpstream(Map<String, List<String>> remoteBranchesUsedAsUpstream) {
        this.remoteBranchesUsedAsUpstream = remoteBranchesUsedAsUpstream;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    public List<String> getAddedBranchNames() {
        return addedBranchNames;
    }

    public void setAddedBranchNames(List<String> addedBranchNames) {
        this.addedBranchNames = addedBranchNames;
    }

    public List<String> getDeletedBranchNames() {
        return deletedBranchNames;
    }

    public void setDeletedBranchNames(List<String> deletedBranchNames) {
        this.deletedBranchNames = deletedBranchNames;
    }
}
