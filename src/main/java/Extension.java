import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.ui.contextmenu.AuditIssueContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Extension implements BurpExtension
{
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        String extensionName = "Count unique issues";

        montoyaApi.extension().setName(extensionName);

        montoyaApi.userInterface().registerContextMenuItemsProvider(new ContextMenuItemsProvider()
        {
            @Override
            public java.util.List<Component> provideMenuItems(AuditIssueContextMenuEvent event)
            {
                JMenuItem menuItem = new JMenuItem(extensionName);
                menuItem.addActionListener(l ->
                {
                    java.util.List<AuditIssue> auditIssues = event.selectedIssues();
                    Map<String, Integer> uniqueIssues = new HashMap<>();

                    auditIssues.forEach(auditIssue -> uniqueIssues.merge(auditIssue.name(), 1, Integer::sum));

                    montoyaApi.logging().logToOutput(uniqueIssues.toString());
                });

                return List.of(menuItem);
            }
        });
    }
}
