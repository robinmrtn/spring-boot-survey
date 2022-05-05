package com.roal.survey_engine.domain.survey.service;

import com.roal.survey_engine.domain.survey.dto.workspace.CreateWorkspaceDto;
import com.roal.survey_engine.domain.survey.dto.workspace.WorkspaceDto;
import com.roal.survey_engine.domain.survey.dto.workspace.WorkspaceDtoMapper;
import com.roal.survey_engine.domain.survey.entity.Workspace;
import com.roal.survey_engine.domain.survey.exception.WorkspaceNotFoundException;
import com.roal.survey_engine.domain.survey.repository.WorkspaceRepository;
import com.roal.survey_engine.domain.user.entity.User;
import com.roal.survey_engine.domain.user.exception.ForbiddenUserActionException;
import com.roal.survey_engine.domain.user.service.UserService;
import com.roal.survey_engine.security.AuthenticationFacade;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceDtoMapper workspaceDtoMapper;
    private final Hashids workspaceHashid;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;


    public WorkspaceService(WorkspaceRepository workspaceRepository, WorkspaceDtoMapper workspaceDtoMapper,
                            @Qualifier("workspaceHashids") Hashids workspaceHashid, UserService userService, AuthenticationFacade authenticationFacade) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceDtoMapper = workspaceDtoMapper;
        this.workspaceHashid = workspaceHashid;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @Transactional
    public WorkspaceDto create(CreateWorkspaceDto workspaceDto) {
        if (!authenticationFacade.isAdmin()) {
            throw new ForbiddenUserActionException();
        }
        Workspace workspace = workspaceDtoMapper.dtoToEntity(workspaceDto);
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        return workspaceDtoMapper.entityToDto(savedWorkspace);
    }

    @Transactional
    public void deleteById(String hashid) {
        if (!authenticationFacade.isAdmin()) {
            throw new ForbiddenUserActionException();
        }
        long id = hashidToId(hashid);
        workspaceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Workspace getEntityByHashid(String hashid) {
        long id = hashidToId(hashid);
        return workspaceRepository.findById(id)
            .orElseThrow(() -> new WorkspaceNotFoundException(hashid));
    }

    private long hashidToId(String hashid) {
        long[] hashids = workspaceHashid.decode(hashid);
        if (hashids.length == 0) {
            throw new WorkspaceNotFoundException(hashid);
        }
        return hashids[0];
    }

    public String idToHashid(long id) {
        return workspaceHashid.encode(id);
    }

    @Transactional
    public WorkspaceDto addUser(String hashid, String userId) {

        if (!currentUserCanModifyWorkspace(hashid)) {
            throw new ForbiddenUserActionException();
        }

        long id = hashidToId(hashid);
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(() -> new WorkspaceNotFoundException(hashid));
        User user = userService.findById(userId);
        workspace.addUser(user);

        return workspaceDtoMapper.entityToDto(workspace);
    }

    @Transactional
    public void deleteUserFromWorkspace(String hashid, String userId) {
        long id = hashidToId(hashid);
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(() -> new WorkspaceNotFoundException(hashid));
        User user = userService.findById(userId);
        workspace.removeUser(user);
    }

    @Transactional(readOnly = true)
    public boolean currentUserCanModifyWorkspace(Workspace workspace) {
        String username = authenticationFacade.getUserDetails()
            .getUsername();
        return authenticationFacade.isAdmin() || workspace.containsUserByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean currentUserCanModifyWorkspace(String hashid) {
        Workspace workspace = getEntityByHashid(hashid);
        return currentUserCanModifyWorkspace(workspace);
    }

    @Transactional(readOnly = true)
    public List<Workspace> getWorkspacesForCurrentUser() {
        String username = authenticationFacade.getUserDetails()
            .getUsername();

        return workspaceRepository.findByUsername(username);
    }
}
