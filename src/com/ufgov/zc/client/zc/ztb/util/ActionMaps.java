package com.ufgov.zc.client.zc.ztb.util;

import com.ufgov.zc.client.zc.zbfile.zbCommonAction;
import com.ufgov.zc.client.zc.ztb.component.CommonAction;
import com.ufgov.zc.client.zc.ztb.component.MainPanel;
import com.ufgov.zc.client.zc.ztb.model.SmartTreeNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionMaps {
    private static Action exportProject = null;

    private static Action exportPack = null;

    private static Action deleteProject = null;

    private static Action deleteProjectBag = null;

    private static Action importToNode = null;

    private static Action addNode = null;

    private static Action uploadProject = null;

    private static Action uploadPack = null;

    private static Action deleteBagDetails = null;

    private static Action dbImportProjects = null;

    private static Action exitProgram = null;

    private static Action savePackDetails = null;

    private static Action toolLocalConfig = null;

    private static Action aboutInfo = null;

    private static Action dbImportRequirement = null;

    private static Action dbImportFormula = null;

    private static Action addEctbBagDetails = null;

    private static Action exportEctbBagDetails = null;

    private static Action exportZbBook = null;

    private static Action packSaveAsTemplate = null;

    private static Action packCopyFromTemplate = null;

    private static Action deleteNode = null;

    private static Action renameByBtn = null;

    private static Action moveup = null;

    private static Action movedown = null;

    private static Action moveuptofirst = null;

    private static Action movedowntolast = null;

    private static Action addNodeRP = null;

    private static Action deleteNodeRP = null;

    private static Action moveupRP = null;

    private static Action movedownRP = null;

    private static Action moveuptofirstRP = null;

    private static Action movedowntolastRP = null;

    private static Action fillMold = null;

    private static Action lockWord = null;

    private static Action unLockWord = null;

    private static Action goonUploadPackAndBid = null;

    private static Action exportTBProject = null;

    private static Action uploadProjectAndBid = null;

    private static Action goonUploadProjectAndBid = null;

    private static Action uploadEcbj = null;

    private static Action uploadZBFile = null;

    private static Action fetchPubKey = null;

    private static Action copytosamenode = null;

    private static Action displayAllNode = null;

    private static Action hideCurrNode = null;

    private static Action wordsMerge = null;

    static Map<String, Action> actions = new HashMap<String, Action>();

    public static void initZB() {
        exportProject = new CommonAction("exportProject", "exportzb");
        actions.put("exportProject", exportProject);
        deleteProject = new CommonAction("deleteProject", "delete");
        actions.put("deleteProject", deleteProject);
        deleteProjectBag = new CommonAction("deleteProjectBag", "delete");
        actions.put("deleteProjectBag", deleteProjectBag);
        importToNode = new CommonAction("importToNode", "importToNode");
        actions.put("importToNode", importToNode);
        addNode = new CommonAction("addNode", "add");
        actions.put("addNode", addNode);
        uploadProject = new CommonAction("uploadProject", "upload");
        actions.put("uploadProject", uploadProject);
        deleteBagDetails = new CommonAction("deleteBagDetails", "delete");
        actions.put("deleteBagDetails", deleteBagDetails);
        dbImportProjects = new CommonAction("dbImportProjects", "dbimport");
        actions.put("dbImportProjects", dbImportProjects);
        exitProgram = new CommonAction("exitProgram", "exit");
        actions.put("exitProgram", exitProgram);
        savePackDetails = new CommonAction("savePackDetails", "save");
        actions.put("savePackDetails", savePackDetails);
        toolLocalConfig = new CommonAction("toolLocalConfig", "dbconfig");
        actions.put("toolLocalConfig", toolLocalConfig);
        aboutInfo = new CommonAction("aboutInfo", "about");
        actions.put("aboutInfo", aboutInfo);
        dbImportRequirement = new CommonAction("dbImportRequirement", "dbImpReq");
        actions.put("dbImportRequirement", dbImportRequirement);
        dbImportFormula = new CommonAction("dbImportFormula", "dbFormula");
        actions.put("dbImportFormula", dbImportFormula);

        packSaveAsTemplate = new CommonAction("packSaveAsTemplate", "saveAsTemplate");
        actions.put("packSaveAsTemplate", packSaveAsTemplate);
        packCopyFromTemplate = new CommonAction("packCopyFromTemplate", "copyTemplate");
        actions.put("packCopyFromTemplate", packCopyFromTemplate);
        deleteNode = new CommonAction("deleteNode", "delete");
        actions.put("deleteNode", deleteNode);
        renameByBtn = new CommonAction("renameByBtn", "renameByBtn");
        actions.put("renameByBtn", renameByBtn);
        moveup = new CommonAction("moveup", "moveup");
        actions.put("moveup", moveup);
        movedown = new CommonAction("movedown", "movedown");
        actions.put("movedown", movedown);
        moveuptofirst = new CommonAction("moveuptofirst", "moveuptofirst");
        actions.put("moveuptofirst", moveuptofirst);
        movedowntolast = new CommonAction("movedowntolast", "movedowntolast");
        actions.put("movedowntolast", movedowntolast);
        fillMold = new CommonAction("fillMold", "fillMold");
        actions.put("fillMold", fillMold);
        lockWord = new CommonAction("lockWord", "lockWord");
        actions.put("lockWord", lockWord);
        unLockWord = new CommonAction("unLockWord", "unLockWord");
        actions.put("unLockWord", unLockWord);
        uploadZBFile = new CommonAction("uploadZBFile", "uploadZBFile");
        actions.put("uploadZBFile", uploadZBFile);
        fetchPubKey = new CommonAction("fetchPubKey", "fetchPubKey");
        actions.put("fetchPubKey", fetchPubKey);
        copytosamenode = new CommonAction("copyToSameNode", "copyToSameNode");
        actions.put("copyToSameNode", copytosamenode);
        actions.put("exportZBFile", exportProject);
        actions.put("uploadZBFile", uploadProject);
        exportZbBook = new CommonAction("exportZbBook", "exportZbBook");
        actions.put("exportZbBook", exportZbBook);
    }

    /**
     * @return void 返回类型
     * @Description: 招标文件审核右键功能
     * @since 1.0
     */
    public static void initZbAudit(String zbFileDownLoadPath, String projCode) {
        exportProject = new zbCommonAction("exportProject", "export", zbFileDownLoadPath, projCode);
        actions.put("exportProject", exportProject);
        addNode = new zbCommonAction("addNode", "add", zbFileDownLoadPath, projCode);
        actions.put("addNode", addNode);
        deleteNode = new zbCommonAction("deleteNode", "delete", zbFileDownLoadPath, projCode);
        actions.put("deleteNode", deleteNode);
        moveup = new zbCommonAction("moveup", "moveup", zbFileDownLoadPath, projCode);
        actions.put("moveup", moveup);
        movedown = new zbCommonAction("movedown", "movedown", zbFileDownLoadPath, projCode);
        actions.put("movedown", movedown);
        moveuptofirst = new zbCommonAction("moveuptofirst", "moveuptofirst", zbFileDownLoadPath, projCode);
        actions.put("moveuptofirst", moveuptofirst);
        movedowntolast = new zbCommonAction("movedowntolast", "movedowntolast", zbFileDownLoadPath, projCode);
        actions.put("movedowntolast", movedowntolast);
        lockWord = new zbCommonAction("lockWord", "lockWord", zbFileDownLoadPath, projCode);
        actions.put("lockWord", lockWord);
        unLockWord = new zbCommonAction("unLockWord", "unLockWord", zbFileDownLoadPath, projCode);
        actions.put("unLockWord", unLockWord);
    }

    public static void initTB() {
        exportProject = new CommonAction("exportProject", "exporttb");
        actions.put("exportProject", exportProject);
        exportPack = new CommonAction("exportPack", "exporttb");
        actions.put("exportPack", exportPack);
        deleteProject = new CommonAction("deleteProject", "delete");
        actions.put("deleteProject", deleteProject);
        deleteProjectBag = new CommonAction("deleteProjectBag", "delete");
        actions.put("deleteProjectBag", deleteProjectBag);
        addNodeRP = new CommonAction("addNodeRP", "add");
        actions.put("addNodeRP", addNodeRP);
        uploadProject = new CommonAction("uploadProject", "upload");
        actions.put("uploadProject", uploadProject);
        uploadPack = new CommonAction("uploadPack", "uploadAndBid");
        actions.put("uploadPack", uploadPack);
        deleteBagDetails = new CommonAction("deleteBagDetails", "delete");
        actions.put("deleteBagDetails", deleteBagDetails);
        dbImportProjects = new CommonAction("dbImportProjects", "dbimport");
        actions.put("dbImportProjects", dbImportProjects);
        exitProgram = new CommonAction("exitProgram", "exit");
        actions.put("exitProgram", exitProgram);
        savePackDetails = new CommonAction("savePackDetails", "save");
        actions.put("savePackDetails", savePackDetails);
        aboutInfo = new CommonAction("aboutInfo", "about");
        actions.put("aboutInfo", aboutInfo);
        addEctbBagDetails = new CommonAction("addEctbBagDetails", "addEctbBagDetails");
        actions.put("addEctbBagDetails", addEctbBagDetails);
        exportEctbBagDetails = new CommonAction("exportEctbBagDetails", "exportEctbBagDetails");
        actions.put("exportEctbBagDetails", exportEctbBagDetails);
        moveupRP = new CommonAction("moveupRP", "moveup");
        actions.put("moveupRP", moveupRP);
        movedownRP = new CommonAction("movedownRP", "movedown");
        actions.put("movedownRP", movedownRP);
        moveuptofirstRP = new CommonAction("moveuptofirstRP", "moveuptofirst");
        actions.put("moveuptofirstRP", moveuptofirstRP);
        movedowntolastRP = new CommonAction("movedowntolastRP", "movedowntolast");
        actions.put("movedowntolastRP", movedowntolastRP);
        deleteNodeRP = new CommonAction("deleteNodeRP", "delete");
        actions.put("deleteNodeRP", deleteNodeRP);
        goonUploadPackAndBid = new CommonAction("goonUploadPackAndBid", "goonUploadPackAndBid");
        actions.put("goonUploadPackAndBid", goonUploadPackAndBid);
        //exportTBProject = new CommonAction("exportTBProject", "exportTBProject");
        //uploadProjectAndBid = new CommonAction("uploadProjectAndBid", "uploadProjectAndBid");
        //goonUploadProjectAndBid = new CommonAction("goonUploadProjectAndBid", "goonUploadProjectAndBid");
        uploadEcbj = new CommonAction("uploadEcbj", "uploadEcbj");
        actions.put("uploadEcbj", uploadEcbj);
        copytosamenode = new CommonAction("copyToSameNode", "copyToSameNode");
        actions.put("copyToSameNode", copytosamenode);
        displayAllNode = new CommonAction("displayAllNode", "displayAllNode");
        actions.put("displayAllNodes", displayAllNode);
        hideCurrNode = new CommonAction("hideCurrNode", "hideCurrNode");
        actions.put("hideCurrNode", hideCurrNode);
        wordsMerge = new CommonAction("mergeWords", "mergeword");
        actions.put("mergeWords", wordsMerge);
    }

    public static void initTTB() {
        toolLocalConfig = new CommonAction("toolLocalConfig", "dbconfig");
        actions.put("toolLocalConfig", toolLocalConfig);
        uploadPack = new CommonAction("uploadPack", "uploadAndBid");
        actions.put("uploadPack", uploadPack);
        uploadEcbj = new CommonAction("uploadEcbj", "uploadEcbj");
        actions.put("uploadEcbj", uploadEcbj);
    }

    public static Map<String, Action> getActionsMap() {
        return actions;
    }

    public static List<Action> getRightMouseClickActionsForTreeNode(String rootNodeType, SmartTreeNode currNode, MainPanel currPanel) {
        String display = currPanel.getClass().getSimpleName();
        if ("ZBPanel".equals(display) || "ZcEbZbFilePanel".equals(display)) {
            return getZBRightMouseClickActionsForTreeNode(rootNodeType, currNode);
        } else if ("TBPanel".equals(display)) {
            return getTBRightMouseClickActionsForTreeNode(rootNodeType, currNode);
        } else if ("EV".equals(display)) {
            return getPBRightMouseClickActionsForTreeNode(rootNodeType, currNode);
        }
        return null;
    }

    public static List<Action> getRightMouseClickActionsForExportZBBook(String rootNodeType, SmartTreeNode currNode, MainPanel currPanel) {
        String display = currPanel.getClass().getSimpleName();
        if ("ZBPanel".equals(display) || "ZcEbZbFilePanel".equals(display)) {
            return getZBRightMouseClickActionsForExportZBBook(rootNodeType, currNode);
        } else if ("TBPanel".equals(display)) {
            return getTBRightMouseClickActionsForTreeNode(rootNodeType, currNode);
        } else if ("EV".equals(display)) {
            return getPBRightMouseClickActionsForTreeNode(rootNodeType, currNode);
        }
        return null;
    }

    //  LIUYUNBIN  招标右键鼠标点击事件
    public static List<Action> getZBRightMouseClickActionsForExportZBBook(String type, SmartTreeNode currNode) {
        String nodeType = currNode.getNodeType();
        List<Action> actions = new ArrayList<Action>();
        if (GV.NODE_TYPE_PROJECT_LIST.equals(type)) {
            if (GV.NODE_TYPE_PROJECT.equals(nodeType) || (GV.NODE_TYPE_DOC.equals(nodeType)&&GV.NODE_NAME_ZB.indexOf(currNode.getNodeName()) >= 0) || GV.NODE_TYPE_ZB.equals(nodeType) || GV.NODE_TYPE_TB.equals(nodeType)) {
                actions.add(exportZbBook);
            }
        }
        return actions;
    }

    //  LIUYUNBIN  招标右键鼠标点击事件
    public static List<Action> getZBRightMouseClickActionsForTreeNode(String type, SmartTreeNode currNode) {
        String nodeType = currNode.getNodeType();
        List<Action> actions = new ArrayList<Action>();
        if (GV.NODE_TYPE_PROJECT_LIST.equals(type)) {
            if (GV.NODE_TYPE_PROJECT.equals(nodeType)) {
                actions.add(addNode);
                actions.add(importToNode);
                actions.add(null);
                actions.add(exportZbBook);
                actions.add(exportProject);
                actions.add(uploadProject);
                if (!currNode.isLocked()) {
                    actions.add(null);
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_PROPACK.equals(nodeType)) {
                actions.add(addNode);
                actions.add(importToNode);
                actions.add(null);
                actions.add(packSaveAsTemplate);
                actions.add(packCopyFromTemplate);
                if (!currNode.isLocked()) {
                    actions.add(null);
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_PACK.equals(nodeType)) {
                actions.add(addNode);
                actions.add(importToNode);
                actions.add(copytosamenode);
                actions.add(null);
                actions.add(packSaveAsTemplate);
                actions.add(packCopyFromTemplate);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
                if (!currNode.isLocked()) {
                    actions.add(null);
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_DOC.equals(nodeType)) {
                actions.add(copytosamenode);
                if (GV.NODE_NAME_ZB.indexOf(currNode.getNodeName()) >= 0) {
                    actions.add(exportZbBook);
                }
                if ((!currNode.isLocked()) || GV.NODE_SOURCE_FORM_SELF == currNode.getNodeSource() || GV.NODE_SOURCE_FORM_OTHER == currNode.getNodeSource()) {
                    actions.add(moveuptofirst);
                    actions.add(moveup);
                    actions.add(movedown);
                    actions.add(movedowntolast);
                    actions.add(null);
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_TABLE.equals(nodeType)) {
                actions.add(copytosamenode);
                if (!currNode.isLocked() || GV.NODE_SOURCE_FORM_SELF == currNode.getNodeSource() || GV.NODE_SOURCE_FORM_OTHER == currNode.getNodeSource()) {
                    actions.add(null);
                    actions.add(moveuptofirst);
                    actions.add(moveup);
                    actions.add(movedown);
                    actions.add(movedowntolast);
                    actions.add(null);
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_TBYLB.equals(nodeType)) {
                actions.add(copytosamenode);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
                if (!currNode.isLocked() || GV.NODE_SOURCE_FORM_SELF == currNode.getNodeSource() || GV.NODE_SOURCE_FORM_OTHER == currNode.getNodeSource()) {
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_PACK_BUSINESS.equals(nodeType)) {
                actions.add(addNode);
                actions.add(copytosamenode);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
            } else if (GV.NODE_TYPE_PACK_TECH.equals(nodeType)) {
                actions.add(addNode);
                actions.add(copytosamenode);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
            } else if (GV.NODE_TYPE_FOLDER.equals(nodeType)) {
                actions.add(addNode);
                actions.add(importToNode);
                actions.add(copytosamenode);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
                if (!currNode.isLocked() || GV.NODE_SOURCE_FORM_SELF == currNode.getNodeSource() || GV.NODE_SOURCE_FORM_OTHER == currNode.getNodeSource()) {
                    actions.add(null);
                    actions.add(deleteNode);
                }
            } else if (GV.NODE_TYPE_DIR.equals(nodeType)) {
                actions.add(addNode);
                actions.add(importToNode);
                actions.add(copytosamenode);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
                actions.add(null);
                actions.add(deleteNode);
            } else if (GV.NODE_TYPE_ZB.equals(nodeType) || GV.NODE_TYPE_TB.equals(nodeType)) {
                actions.add(addNode);
                actions.add(importToNode);
                actions.add(copytosamenode);
                actions.add(exportZbBook);
                actions.add(null);
                actions.add(moveuptofirst);
                actions.add(moveup);
                actions.add(movedown);
                actions.add(movedowntolast);
                actions.add(null);
                actions.add(deleteNode);
            } else {
                actions.add(null);
                actions.add(deleteNode);
            }
        }
        return actions;
    }

    public static List<Action> getTBRightMouseClickActionsForTreeNode(String type, SmartTreeNode currNode) {
        String nodeType = currNode.getNodeType();
        List<Action> actions = new ArrayList<Action>();
        if (GV.NODE_TYPE_PROJECT.equals(nodeType)) {
            //actions.add(exportTBProject);
            //actions.add(uploadProjectAndBid);
            //actions.add(goonUploadProjectAndBid);
        } else if (GV.NODE_TYPE_PACK.equals(nodeType)) {
            actions.add(null);
            actions.add(wordsMerge);
            actions.add(addEctbBagDetails);
            //actions.add(exportEctbBagDetails);
            actions.add(exportPack);
            actions.add(uploadPack);
            actions.add(goonUploadPackAndBid);
        } else if (GV.NODE_TYPE_ECBJB.equals(nodeType)) {
            actions.add(deleteNode);
            actions.add(null);
            actions.add(exportEctbBagDetails);
            actions.add(uploadEcbj);
        } else if (GV.NODE_TYPE_DOC.equals(nodeType) || GV.NODE_TYPE_DIR.equals(nodeType) || GV.NODE_TYPE_FOLDER.equals(nodeType)) {
            actions.add(copytosamenode);
        } else if (GV.NODE_TYPE_TB.equals(nodeType)) {
            actions.add(wordsMerge);
            actions.add(copytosamenode);
        }
        return actions;
    }

    public static List<Action> getPBRightMouseClickActionsForTreeNode(String type, SmartTreeNode currNode) {
        List<Action> actions = new ArrayList<Action>();
        return actions;
    }

    public static List<Action> getRClickActionsForRPTreeNode(SmartTreeNode currNode) {
        List<Action> actions = new ArrayList<Action>();
        String nodeType = currNode.getNodeType();
        if (GV.NODE_TYPE_RP_ROOT.equals(nodeType)) {
            actions.add(displayAllNode);
            return actions;
        } else if (GV.NODE_TYPE_PACK_RP.equals(nodeType)) {
            actions.add(addNodeRP);
            actions.add(null);
            actions.add(displayAllNode);
            actions.add(hideCurrNode);
            return actions;
        } else {
            actions.addAll(getBaseActions());
            actions.add(hideCurrNode);
        }
        return actions;
    }

    private static List<Action> getBaseActions() {
        List<Action> actions = new ArrayList<Action>();
        actions.add(addNodeRP);
        actions.add(null);
        actions.add(moveuptofirstRP);
        actions.add(moveupRP);
        actions.add(movedownRP);
        actions.add(movedowntolastRP);
        actions.add(null);
        actions.add(deleteNodeRP);
        return actions;
    }
}
