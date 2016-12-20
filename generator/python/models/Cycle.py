
class Cycle(object):

    def __init__(self, name, environment, build, version_id, project_id, description, sprint_id):
        self.name = name
        self.environment = environment
        self.build = build
        self.versionId = version_id
        self.projectId = project_id
        self.description = description
        self.sprintId = sprint_id
        self.createdBy = None
        self.modifiedBy = None
        self.startDate = None
        self.endDate = None
