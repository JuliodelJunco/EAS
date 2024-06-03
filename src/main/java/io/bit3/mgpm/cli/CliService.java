package io.bit3.mgpm.cli;

import io.bit3.mgpm.worker.Worker;

import java.util.*;

public class CliService {

    public List<CliAux> printRemoteBranches(Map<String, List<String>> remoteBranchNames, Map<String, List<String>> addedRemoteBranchNames, Map<String, List<String>> deletedRemoteBranchNames, Map<String, List<String>> remoteBranchesUsedAsUpstream) {
        List<CliAux> cliAuxList = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : remoteBranchNames.entrySet()) {
            CliAux cliAux = new CliAux();
            cliAux.setRemoteName(entry.getKey());
            List<String> currentBranchNames = entry.getValue();
            cliAux.setAddedBranchNames(addedRemoteBranchNames.get(cliAux.getRemoteName()));
            cliAux.setDeletedBranchNames(deletedRemoteBranchNames.get(cliAux.getRemoteName()));

            Set<String> remoteBranches = new TreeSet<>();
            remoteBranches.addAll(currentBranchNames);
            if (null != cliAux.getAddedBranchNames()) {
                remoteBranches.addAll(cliAux.getAddedBranchNames());
            }
            if (null != cliAux.getDeletedBranchNames()) {
                remoteBranches.addAll(cliAux.getDeletedBranchNames());
            }
            cliAux.setRemoteBranches(remoteBranches);
            cliAux.setRemoteBranchesUsedAsUpstream(remoteBranchesUsedAsUpstream);

            cliAuxList.add(cliAux);

        }
        return cliAuxList;
    }
    public boolean printDetails(Map<String, List<String>> added,Map<String, List<String>> deleted,Map<String, Worker.Stats> stats ){

        return !added.isEmpty()
                || !deleted.isEmpty()
                || !stats.values().stream().map(Worker.Stats::isEmpty).reduce(true, (a, b) -> a && b);
    }
}
