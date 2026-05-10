package iso15939.data;

import iso15939.model.Dimension;
import iso15939.model.Direction;
import iso15939.model.Metric;
import iso15939.model.Mode;
import iso15939.model.QualityType;
import iso15939.model.Scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioRepository {
    private final Map<Mode, List<Scenario>> scenariosByMode = new HashMap<>();

    public ScenarioRepository() {
        addScenario(createEducationAlpha());
        addScenario(createEducationGamma());
        addScenario(createEducationBeta());
        addScenario(createEducationDelta());
        addScenario(createHealthNorth());
        addScenario(createHealthWest());
        addScenario(createHealthEast());
        addScenario(createHealthSouth());
    }

    public List<Scenario> findByMode(Mode mode) {
        return new ArrayList<>(scenariosByMode.getOrDefault(mode, new ArrayList<>()));
    }

    public List<Scenario> findByModeAndQualityType(Mode mode, QualityType qualityType) {
        List<Scenario> filtered = new ArrayList<>();
        for (Scenario scenario : scenariosByMode.getOrDefault(mode, new ArrayList<>())) {
            if (scenario.getQualityType() == qualityType) {
                filtered.add(scenario);
            }
        }
        return filtered;
    }

    private void addScenario(Scenario scenario) {
        scenariosByMode.computeIfAbsent(scenario.getMode(), key -> new ArrayList<>()).add(scenario);
    }

    private Scenario createEducationAlpha() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Usability", 25, List.of(
                new Metric("SUS score", 50, Direction.HIGHER_BETTER, 0, 100, "points", 89),
                new Metric("Onboarding time", 50, Direction.LOWER_BETTER, 0, 60, "min", 5)
        )));
        dimensions.add(new Dimension("Perf. Efficiency", 20, List.of(
                new Metric("Video start time", 50, Direction.LOWER_BETTER, 0, 15, "sec", 3),
                new Metric("Concurrent exams", 50, Direction.HIGHER_BETTER, 0, 600, "users", 520)
        )));
        dimensions.add(new Dimension("Accessibility", 20, List.of(
                new Metric("WCAG compliance", 50, Direction.HIGHER_BETTER, 0, 100, "%", 86),
                new Metric("Screen reader score", 50, Direction.HIGHER_BETTER, 0, 100, "%", 78)
        )));
        dimensions.add(new Dimension("Reliability", 20, List.of(
                new Metric("Uptime", 50, Direction.HIGHER_BETTER, 95, 100, "%", 99.3),
                new Metric("MTTR", 50, Direction.LOWER_BETTER, 0, 120, "min", 18)
        )));
        dimensions.add(new Dimension("Func. Suitability", 15, List.of(
                new Metric("Feature completion", 50, Direction.HIGHER_BETTER, 0, 100, "%", 91),
                new Metric("Assignment submit rate", 50, Direction.HIGHER_BETTER, 0, 100, "%", 88)
        )));
        return new Scenario("Scenario C - Team Alpha", QualityType.PRODUCT, Mode.EDUCATION,
                "Learning management system product quality measurement.", dimensions);
    }

    private Scenario createEducationBeta() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Sprint Predictability", 30, List.of(
                new Metric("Planned story completion", 60, Direction.HIGHER_BETTER, 0, 100, "%", 72),
                new Metric("Carry-over stories", 40, Direction.LOWER_BETTER, 0, 30, "items", 9)
        )));
        dimensions.add(new Dimension("Code Quality", 25, List.of(
                new Metric("Static analysis pass rate", 50, Direction.HIGHER_BETTER, 0, 100, "%", 81),
                new Metric("Critical defects", 50, Direction.LOWER_BETTER, 0, 20, "defects", 5)
        )));
        dimensions.add(new Dimension("Team Collaboration", 25, List.of(
                new Metric("Review participation", 50, Direction.HIGHER_BETTER, 0, 100, "%", 76),
                new Metric("Blocked task time", 50, Direction.LOWER_BETTER, 0, 80, "hours", 19)
        )));
        dimensions.add(new Dimension("Release Discipline", 20, List.of(
                new Metric("Build success rate", 50, Direction.HIGHER_BETTER, 0, 100, "%", 84),
                new Metric("Rollback count", 50, Direction.LOWER_BETTER, 0, 10, "rollbacks", 2)
        )));
        return new Scenario("Scenario D - Team Beta", QualityType.PROCESS, Mode.EDUCATION,
                "LMS development process quality measurement.", dimensions);
    }

    private Scenario createEducationGamma() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Learnability", 25, List.of(
                new Metric("First quiz success", 50, Direction.HIGHER_BETTER, 0, 100, "%", 82),
                new Metric("Help requests", 50, Direction.LOWER_BETTER, 0, 40, "requests", 11)
        )));
        dimensions.add(new Dimension("Responsiveness", 20, List.of(
                new Metric("Page load time", 50, Direction.LOWER_BETTER, 0, 8, "sec", 2),
                new Metric("Peak active users", 50, Direction.HIGHER_BETTER, 0, 800, "users", 610)
        )));
        dimensions.add(new Dimension("Content Access", 20, List.of(
                new Metric("Material availability", 50, Direction.HIGHER_BETTER, 0, 100, "%", 93),
                new Metric("Download failure rate", 50, Direction.LOWER_BETTER, 0, 20, "%", 4)
        )));
        dimensions.add(new Dimension("Reliability", 20, List.of(
                new Metric("Session success rate", 50, Direction.HIGHER_BETTER, 90, 100, "%", 97),
                new Metric("Error reports", 50, Direction.LOWER_BETTER, 0, 100, "reports", 21)
        )));
        dimensions.add(new Dimension("Functional Fit", 15, List.of(
                new Metric("Course setup completion", 50, Direction.HIGHER_BETTER, 0, 100, "%", 89),
                new Metric("Grade sync success", 50, Direction.HIGHER_BETTER, 0, 100, "%", 84)
        )));
        return new Scenario("Scenario E - Team Gamma", QualityType.PRODUCT, Mode.EDUCATION,
                "Alternative LMS product quality measurement.", dimensions);
    }

    private Scenario createEducationDelta() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Planning Quality", 30, List.of(
                new Metric("Requirement readiness", 60, Direction.HIGHER_BETTER, 0, 100, "%", 79),
                new Metric("Scope changes", 40, Direction.LOWER_BETTER, 0, 25, "changes", 7)
        )));
        dimensions.add(new Dimension("Review Quality", 25, List.of(
                new Metric("Review coverage", 50, Direction.HIGHER_BETTER, 0, 100, "%", 85),
                new Metric("Reopened review items", 50, Direction.LOWER_BETTER, 0, 30, "items", 6)
        )));
        dimensions.add(new Dimension("Delivery Flow", 25, List.of(
                new Metric("On-time tasks", 50, Direction.HIGHER_BETTER, 0, 100, "%", 73),
                new Metric("Blocked days", 50, Direction.LOWER_BETTER, 0, 60, "days", 16)
        )));
        dimensions.add(new Dimension("Release Quality", 20, List.of(
                new Metric("Release checklist pass", 50, Direction.HIGHER_BETTER, 0, 100, "%", 88),
                new Metric("Post-release fixes", 50, Direction.LOWER_BETTER, 0, 20, "fixes", 5)
        )));
        return new Scenario("Scenario F - Team Delta", QualityType.PROCESS, Mode.EDUCATION,
                "Alternative LMS process quality measurement.", dimensions);
    }

    private Scenario createHealthNorth() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Security", 25, List.of(
                new Metric("MFA coverage", 50, Direction.HIGHER_BETTER, 0, 100, "%", 94),
                new Metric("Open vulnerabilities", 50, Direction.LOWER_BETTER, 0, 50, "issues", 6)
        )));
        dimensions.add(new Dimension("Availability", 25, List.of(
                new Metric("System uptime", 60, Direction.HIGHER_BETTER, 95, 100, "%", 99.1),
                new Metric("Incident recovery", 40, Direction.LOWER_BETTER, 0, 180, "min", 34)
        )));
        dimensions.add(new Dimension("Data Integrity", 20, List.of(
                new Metric("Record accuracy", 50, Direction.HIGHER_BETTER, 0, 100, "%", 92),
                new Metric("Duplicate records", 50, Direction.LOWER_BETTER, 0, 300, "records", 48)
        )));
        dimensions.add(new Dimension("Clinical Usability", 15, List.of(
                new Metric("Task success rate", 50, Direction.HIGHER_BETTER, 0, 100, "%", 87),
                new Metric("Patient lookup time", 50, Direction.LOWER_BETTER, 0, 120, "sec", 28)
        )));
        dimensions.add(new Dimension("Interoperability", 15, List.of(
                new Metric("FHIR message success", 50, Direction.HIGHER_BETTER, 0, 100, "%", 83),
                new Metric("Sync delay", 50, Direction.LOWER_BETTER, 0, 60, "min", 17)
        )));
        return new Scenario("Scenario A - North Clinic", QualityType.PRODUCT, Mode.HEALTH,
                "Hospital information system product quality measurement.", dimensions);
    }

    private Scenario createHealthEast() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Sprint Efficiency", 25, List.of(
                new Metric("Velocity stability", 50, Direction.HIGHER_BETTER, 0, 100, "%", 68),
                new Metric("Cycle time", 50, Direction.LOWER_BETTER, 0, 30, "days", 11)
        )));
        dimensions.add(new Dimension("Testing Discipline", 25, List.of(
                new Metric("Automated test coverage", 50, Direction.HIGHER_BETTER, 0, 100, "%", 74),
                new Metric("Escaped defects", 50, Direction.LOWER_BETTER, 0, 40, "defects", 10)
        )));
        dimensions.add(new Dimension("Compliance Flow", 20, List.of(
                new Metric("Audit checklist pass", 60, Direction.HIGHER_BETTER, 0, 100, "%", 88),
                new Metric("Approval waiting time", 40, Direction.LOWER_BETTER, 0, 20, "days", 6)
        )));
        dimensions.add(new Dimension("Team Collaboration", 15, List.of(
                new Metric("Review turnaround", 50, Direction.LOWER_BETTER, 0, 72, "hours", 21),
                new Metric("Knowledge sharing sessions", 50, Direction.HIGHER_BETTER, 0, 12, "sessions", 8)
        )));
        dimensions.add(new Dimension("Release Stability", 15, List.of(
                new Metric("Deployment success", 50, Direction.HIGHER_BETTER, 0, 100, "%", 86),
                new Metric("Hotfix count", 50, Direction.LOWER_BETTER, 0, 15, "hotfixes", 4)
        )));
        return new Scenario("Scenario B - East Hospital", QualityType.PROCESS, Mode.HEALTH,
                "Healthcare software delivery process quality measurement.", dimensions);
    }

    private Scenario createHealthWest() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Privacy", 25, List.of(
                new Metric("Consent coverage", 50, Direction.HIGHER_BETTER, 0, 100, "%", 91),
                new Metric("Unauthorized access events", 50, Direction.LOWER_BETTER, 0, 40, "events", 3)
        )));
        dimensions.add(new Dimension("Availability", 25, List.of(
                new Metric("Portal uptime", 60, Direction.HIGHER_BETTER, 95, 100, "%", 98.7),
                new Metric("Service interruption", 40, Direction.LOWER_BETTER, 0, 120, "min", 22)
        )));
        dimensions.add(new Dimension("Data Quality", 20, List.of(
                new Metric("Medication accuracy", 50, Direction.HIGHER_BETTER, 0, 100, "%", 95),
                new Metric("Incomplete records", 50, Direction.LOWER_BETTER, 0, 250, "records", 37)
        )));
        dimensions.add(new Dimension("Usability", 15, List.of(
                new Metric("Appointment success", 50, Direction.HIGHER_BETTER, 0, 100, "%", 86),
                new Metric("Search time", 50, Direction.LOWER_BETTER, 0, 90, "sec", 24)
        )));
        dimensions.add(new Dimension("Integration", 15, List.of(
                new Metric("Lab result sync", 50, Direction.HIGHER_BETTER, 0, 100, "%", 89),
                new Metric("Queue delay", 50, Direction.LOWER_BETTER, 0, 90, "min", 19)
        )));
        return new Scenario("Scenario G - West Clinic", QualityType.PRODUCT, Mode.HEALTH,
                "Alternative healthcare product quality measurement.", dimensions);
    }

    private Scenario createHealthSouth() {
        List<Dimension> dimensions = new ArrayList<>();
        dimensions.add(new Dimension("Incident Process", 25, List.of(
                new Metric("Triage within SLA", 50, Direction.HIGHER_BETTER, 0, 100, "%", 82),
                new Metric("Unassigned incidents", 50, Direction.LOWER_BETTER, 0, 30, "incidents", 5)
        )));
        dimensions.add(new Dimension("Testing Flow", 25, List.of(
                new Metric("Regression pass rate", 50, Direction.HIGHER_BETTER, 0, 100, "%", 78),
                new Metric("Failed test reruns", 50, Direction.LOWER_BETTER, 0, 50, "reruns", 14)
        )));
        dimensions.add(new Dimension("Compliance Work", 20, List.of(
                new Metric("Policy review pass", 60, Direction.HIGHER_BETTER, 0, 100, "%", 90),
                new Metric("Open audit actions", 40, Direction.LOWER_BETTER, 0, 25, "actions", 8)
        )));
        dimensions.add(new Dimension("Team Flow", 15, List.of(
                new Metric("Handoff completion", 50, Direction.HIGHER_BETTER, 0, 100, "%", 83),
                new Metric("Waiting time", 50, Direction.LOWER_BETTER, 0, 72, "hours", 18)
        )));
        dimensions.add(new Dimension("Deployment Flow", 15, List.of(
                new Metric("Approved deployments", 50, Direction.HIGHER_BETTER, 0, 100, "%", 87),
                new Metric("Emergency patches", 50, Direction.LOWER_BETTER, 0, 12, "patches", 3)
        )));
        return new Scenario("Scenario H - South Hospital", QualityType.PROCESS, Mode.HEALTH,
                "Alternative healthcare process quality measurement.", dimensions);
    }
}
