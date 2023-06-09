package com.edu.hzau.cocs.fe.service;

import com.edu.hzau.cocs.fe.pojo.datalog.Datalog;
import com.edu.hzau.cocs.fe.pojo.datalog.Relationship;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yue
 */
@Slf4j
@Service
public class DatalogParser {
    public Datalog parseDatalog(String datalogStr) {
        if (datalogStr == null) {
            log.error("> datalog is null");
            return null;
        }
        // 头部
        Datalog datalog = new Datalog();
        List<String> header = Arrays.asList(datalogStr.substring(2, datalogStr.indexOf(":-") - 1).split(","));
        datalog.setHeader(header);

        // 关系和属性
        String body = datalogStr.substring(datalogStr.indexOf(":-") + 2, datalogStr.length() - 1).trim();
        String[] relationships = body.split("relationship:");
        Map<String, Relationship> relationshipMap = new HashMap<>();
        for (int i = 1; i < relationships.length; i++) {
            Relationship rela = new Relationship();
            Map<String, String> attrMap = new HashMap<>();
            String[] re = relationships[i].split("\\),");
            for (int j = 0; j < re.length; j++) {
                if (j == 0) {
                    String relationship = re[j].substring(0, re[j].indexOf("("));
                    rela.setRelationship(relationship);
                } else {
                    if (re[j].contains("<")) {
                        String attrPrefix = re[j].substring(re[j].indexOf(":") + 1, re[j].indexOf("("));
                        String attrSuffix = re[j].substring(re[j].indexOf("<") + 1, re[j].indexOf(">"));
                        attrMap.put(attrPrefix, attrSuffix);
                    }
                }
            }
            rela.setAttributes(attrMap);
            relationshipMap.put(rela.getRelationship(), rela);
        }
        datalog.setRelationships(relationshipMap);
        return datalog;
    }
    @Test
    public void test() {
        DatalogParser datalogParser = new DatalogParser();
        String datalog1 = "?(Microbe_name,Gene_symbol,Gene_kegg_pathway):-\n" +
                "relationship:is_host_of(Swine_index,Microbe_id,<100>),\n" +
                "attribute:p_value_dpf_tpf_difference(Microbe_id,<1>),\n" +
                "attribute:microbe_name(Microbe_id,Microbe_name),\n" +
                "attribute:microbe_time(Microbe_id,<100>),\n" +
                "relationship:changes_the_expression_by_microbiota(Microbe_id,Ncbi_gene_id,<change_the_expression_by_microbiota>),\n" +
                "attribute:gene_symbol(Ncbi_gene_id,Gene_symbol),\n" +
                "relationship:has_gene_kegg_info(Ncbi_gene_id,Gene_kegg_id,<has_gene_kegg_info>),\n" +
                "attribute:gene_kegg_pathway(Gene_kegg_id,Gene_kegg_pathway).";
        String datalog2 = "?(Microbe_name,Gene_symbol,Gene_kegg_pathway):-\n" +
                "relationship:is_host_of(Swine_index,Microbe_id,<80>),\n" +
                "attribute:group(Microbe_id,<A1>),\n" +
                "attribute:p_age_difference(Microbe_id,<1>),\n" +
                "attribute:microbe_name(Microbe_id,Microbe_name),\n" +
                "attribute:microbe_time(Microbe_id,<80>),\n" +
                "relationship:changes_the_expression_by_microbiota(Microbe_id,Ncbi_gene_id,<change_the_expression_by_microbiota>),\n" +
                "attribute:gene_symbol(Ncbi_gene_id,Gene_symbol),\n" +
                "relationship:has_gene_kegg_info(Ncbi_gene_id,Gene_kegg_id,<has_gene_kegg_info>),\n" +
                "attribute:gene_kegg_pathway(Gene_kegg_id,Gene_kegg_pathway).";
        String datalog3 = "?(Microbe_name,Gene_symbol,Gene_kegg_pathway):-\n" +
                "relationship:is_host_of(Swine_index,Microbe_id,<131>),\n" +
                "attribute:group(Microbe_id,<A1>),\n" +
                "attribute:p_age_difference(Microbe_id,<1>),\n" +
                "attribute:microbe_name(Microbe_id,Microbe_name),\n" +
                "attribute:microbe_time(Microbe_id,<131>),\n" +
                "relationship:changes_the_expression_by_microbiota(Microbe_id,Ncbi_gene_id,<change_the_expression_by_microbiota>),\n" +
                "attribute:gene_symbol(Ncbi_gene_id,Gene_symbol),\n" +
                "relationship:has_gene_kegg_info(Ncbi_gene_id,Gene_kegg_id,<has_gene_kegg_info>),\n" +
                "attribute:gene_kegg_pathway(Gene_kegg_id,Gene_kegg_pathway).";
        String datalog4 = "?(Metabolome_name,Metabolome_pathway,Metabolome_pathway_url):-\n" +
                "relationship:generates(Swine_index,Metabolome_index,<generates>),\n" +
                "attribute:metabolome_difference(Metabolome_index,<1>),\n" +
                "attribute:metabolism_name(Metabolome_index,Metabolome_name),\n" +
                "attribute:metabolism_time(Metabolome_index,<155>),\n" +
                "relationship:has_hmdb_info(Hmdb_info_index,Metabolome_index,<has_hmdb_info>),\n" +
                "attribute:metabolome_hmdb_index(Hmdb_info_index,Metabolome_hmdb_index),\n" +
                "attribute:metabolome_pathway(Hmdb_info_index,Metabolome_pathway),\n" +
                "attribute:metabolome_pathway_url(Hmdb_info_index,Metabolome_pathway_url).";
        Datalog datalog = datalogParser.parseDatalog(datalog1);
        System.out.println(datalog);
    }
}
