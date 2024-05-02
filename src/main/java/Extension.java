import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import burp.api.montoya.ui.contextmenu.AuditIssueContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static burp.api.montoya.scanner.audit.issues.AuditIssueSeverity.*;

public class Extension implements BurpExtension
{

    private Logging logging;

    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        String extensionName = "Count unique issues";

        montoyaApi.extension().setName(extensionName);

        logging = montoyaApi.logging();

        montoyaApi.userInterface().registerContextMenuItemsProvider(new ContextMenuItemsProvider()
        {
            @Override
            public List<Component> provideMenuItems(AuditIssueContextMenuEvent event)
            {
                JMenuItem allSeverities = new JMenuItem("All severities");
                allSeverities.addActionListener(l -> sumUniqueIssues(event.selectedIssues()));

                JMenuItem highSeverities = new JMenuItem("High severities");
                highSeverities.addActionListener(l -> sumUniqueIssues(event.selectedIssues(), HIGH));

                JMenuItem mediumSeverities = new JMenuItem("Medium severities");
                mediumSeverities.addActionListener(l -> sumUniqueIssues(event.selectedIssues(), MEDIUM));

                JMenuItem lowSeverities = new JMenuItem("Low severities");
                lowSeverities.addActionListener(l -> sumUniqueIssues(event.selectedIssues(), LOW));

                JMenuItem infoSeverities = new JMenuItem("Information severities");
                infoSeverities.addActionListener(l -> sumUniqueIssues(event.selectedIssues(), INFORMATION));

                return List.of(allSeverities, highSeverities, mediumSeverities, lowSeverities, infoSeverities);
            }
        });
    }

    private void sumUniqueIssues(List<AuditIssue> auditIssues, AuditIssueSeverity severity)
    {
        List<AuditIssue> filteredIssues = auditIssues
                .stream()
                .filter(auditIssue -> auditIssue.severity().equals(severity))
                .toList();

        sumUniqueIssues(filteredIssues);
    }

    private void sumUniqueIssues(List<AuditIssue> auditIssues)
    {
        Map<String, Integer> uniqueIssues = new HashMap<>();

        auditIssues.forEach(auditIssue -> uniqueIssues.merge(auditIssue.name(), 1, Integer::sum));

        logging.logToOutput(uniqueIssues.toString());
    }
}
