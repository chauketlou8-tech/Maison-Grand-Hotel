from pathlib import Path
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.units import mm
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib import colors

# ============================================================
# CONFIGURATION
# ============================================================

BASE_DIR = Path("resources/procedures/data-protection")

HOTEL_NAME = "Mason Hotel"
HOTEL_ADDRESS = "08 Greenwood Street, Gardens, Cape Town, 7700, South Africa"
HOTEL_PHONE = "+27 21 987 6543"
HOTEL_EMAIL = "stay@maisongrandhotel.co.za"

DOCUMENT_FOOTER = (
    f"{HOTEL_NAME}<br/>"
    f"{HOTEL_ADDRESS}<br/>"
    f"{HOTEL_PHONE} | {HOTEL_EMAIL}<br/>"
    "Document Owner: Mason Hotel Data Protection Office<br/>"
    "Review Cycle: Annual or when operational/legal requirements change.<br/>"
    "Version: 1.0"
)

# ============================================================
# STYLES
# ============================================================

styles = getSampleStyleSheet()

hotel_title = ParagraphStyle(
    "HotelTitle",
    parent=styles["Title"],
    alignment=TA_CENTER,
    fontSize=22,
    leading=26,
    spaceAfter=4,
    textColor=colors.HexColor("#1a1814")
)

document_title_style = ParagraphStyle(
    "DocumentTitle",
    parent=styles["Title"],
    alignment=TA_CENTER,
    fontSize=18,
    leading=22,
    spaceAfter=24,
    textColor=colors.HexColor("#c9a84c")
)

section_title = ParagraphStyle(
    "SectionTitle",
    parent=styles["Heading2"],
    fontSize=13,
    leading=16,
    spaceBefore=12,
    spaceAfter=6,
    textColor=colors.HexColor("#1a1814")
)

body = ParagraphStyle(
    "Body",
    parent=styles["BodyText"],
    fontSize=10.5,
    leading=15,
    spaceAfter=8,
    alignment=TA_LEFT
)

footer = ParagraphStyle(
    "Footer",
    parent=styles["BodyText"],
    fontSize=8.5,
    leading=12,
    spaceBefore=20,
    textColor=colors.HexColor("#666666")
)

# ============================================================
# PROCEDURE DATA
# ============================================================

procedures = {

    "personal-data-collection-procedure.pdf": {
        "title": "Personal Data Collection Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for collecting personal data."),
            ("2. Scope", "This procedure applies to all staff who collect personal data."),
            ("3. Collection Principles", "Collect data lawfully, fairly, and transparently."),
            ("4. Process Steps", "Identify data required. Obtain consent if required. Collect data. Record data. Secure data."),
            ("5. Verification", "Verify data is collected lawfully. Check consent."),
            ("6. Confirmation", "Confirm data collection complete."),
            ("7. Documentation", "Record collection activities."),
            ("8. Quality Control", "Review collection for compliance. Ensure all data is collected lawfully.")
        ]
    },

    "personal-data-access-procedure.pdf": {
        "title": "Personal Data Access Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for accessing personal data."),
            ("2. Scope", "This procedure applies to all staff who access personal data."),
            ("3. Access Principles", "Access personal data only for authorised purposes."),
            ("4. Process Steps", "Request access. Verify authorisation. Access data. Log access. Secure data."),
            ("5. Verification", "Verify access is authorised. Check logs."),
            ("6. Confirmation", "Confirm data access complete."),
            ("7. Documentation", "Record access activities."),
            ("8. Quality Control", "Review access for compliance. Ensure all access is authorised.")
        ]
    },

    "personal-data-correction-procedure.pdf": {
        "title": "Personal Data Correction Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for correcting personal data."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Correction Process", "Correct data when inaccurate or incomplete."),
            ("4. Process Steps", "Receive correction request. Verify identity. Correct data. Document correction. Notify data subject."),
            ("5. Verification", "Verify correction is accurate. Check documentation."),
            ("6. Confirmation", "Confirm data correction complete."),
            ("7. Documentation", "Record correction activities."),
            ("8. Quality Control", "Review correction for accuracy. Ensure all corrections are documented.")
        ]
    },

    "personal-data-deletion-procedure.pdf": {
        "title": "Personal Data Deletion Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for deleting personal data."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Deletion Process", "Delete data when no longer required or upon request."),
            ("4. Process Steps", "Receive deletion request. Verify identity. Delete data. Document deletion. Notify data subject."),
            ("5. Verification", "Verify data is deleted. Check documentation."),
            ("6. Confirmation", "Confirm data deletion complete."),
            ("7. Documentation", "Record deletion activities."),
            ("8. Quality Control", "Review deletion for compliance. Ensure data is securely deleted.")
        ]
    },

    "data-retention-procedure.pdf": {
        "title": "Data Retention Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for retaining data."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Retention Process", "Retain data for the required period."),
            ("4. Process Steps", "Identify retention period. Store data securely. Monitor retention. Review regularly. Dispose when no longer required."),
            ("5. Verification", "Verify data is retained correctly. Check documentation."),
            ("6. Confirmation", "Confirm data retention complete."),
            ("7. Documentation", "Record retention activities."),
            ("8. Quality Control", "Review retention for compliance. Ensure data is retained for required period.")
        ]
    },

    "data-disposal-procedure.pdf": {
        "title": "Data Disposal Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for disposing of data."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Disposal Process", "Dispose of data securely when no longer required."),
            ("4. Process Steps", "Identify data for disposal. Verify retention period expired. Dispose of data securely. Document disposal."),
            ("5. Verification", "Verify data is disposed of securely. Check documentation."),
            ("6. Confirmation", "Confirm data disposal complete."),
            ("7. Documentation", "Record disposal activities."),
            ("8. Quality Control", "Review disposal for security. Ensure data is securely disposed.")
        ]
    },

    "data-sharing-procedure.pdf": {
        "title": "Data Sharing Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for sharing data."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Sharing Principles", "Share data only for legitimate purposes."),
            ("4. Process Steps", "Identify purpose. Verify authorisation. Obtain consent if required. Share data securely. Document sharing."),
            ("5. Verification", "Verify sharing is authorised. Check documentation."),
            ("6. Confirmation", "Confirm data sharing complete."),
            ("7. Documentation", "Record sharing activities."),
            ("8. Quality Control", "Review sharing for compliance. Ensure data is shared lawfully.")
        ]
    },

    "third-party-data-processing-procedure.pdf": {
        "title": "Third-Party Data Processing Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for third-party data processing."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Third-Party Processing", "Ensure third parties process data lawfully and securely."),
            ("4. Process Steps", "Select third party. Sign data processing agreement. Provide data. Monitor processing. Review compliance."),
            ("5. Verification", "Verify third party is compliant. Check agreement."),
            ("6. Confirmation", "Confirm third-party processing complete."),
            ("7. Documentation", "Record third-party processing activities."),
            ("8. Quality Control", "Review third-party processing for compliance. Ensure data is protected.")
        ]
    },

    "data-subject-request-procedure.pdf": {
        "title": "Data Subject Request Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for handling data subject requests."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Request Types", "Access, correction, deletion, objection, and portability requests."),
            ("4. Process Steps", "Receive request. Verify identity. Process request. Respond to data subject. Document request."),
            ("5. Verification", "Verify request is processed correctly. Check documentation."),
            ("6. Confirmation", "Confirm data subject request complete."),
            ("7. Documentation", "Record request activities."),
            ("8. Quality Control", "Review requests for compliance. Ensure all requests are responded to.")
        ]
    },

    "data-breach-response-procedure.pdf": {
        "title": "Data Breach Response Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for responding to data breaches."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Breach Response", "Respond to data breaches promptly and effectively."),
            ("4. Process Steps", "Identify breach. Contain breach. Investigate breach. Notify affected individuals. Notify authorities if required. Document breach."),
            ("5. Verification", "Verify breach is contained. Check documentation."),
            ("6. Confirmation", "Confirm data breach response complete."),
            ("7. Documentation", "Record breach response activities."),
            ("8. Quality Control", "Review breach response for effectiveness. Ensure all steps are followed.")
        ]
    },

    "privacy-incident-reporting-procedure.pdf": {
        "title": "Privacy Incident Reporting Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for reporting privacy incidents."),
            ("2. Scope", "This procedure applies to all staff."),
            ("3. Incident Reporting", "Report all privacy incidents immediately."),
            ("4. Process Steps", "Identify incident. Report to Data Protection Officer. Document incident. Investigate. Take corrective action."),
            ("5. Verification", "Verify incident is reported. Check documentation."),
            ("6. Confirmation", "Confirm privacy incident reporting complete."),
            ("7. Documentation", "Record incident reporting activities."),
            ("8. Quality Control", "Review incident reporting for compliance. Ensure all incidents are reported.")
        ]
    },

    "data-protection-impact-assessment-procedure.pdf": {
        "title": "Data Protection Impact Assessment Procedure",
        "sections": [
            ("1. Purpose", "This procedure outlines the steps for conducting data protection impact assessments."),
            ("2. Scope", "This procedure applies to the Data Protection Office."),
            ("3. Assessment Process", "Assess risks of data processing activities."),
            ("4. Process Steps", "Identify processing activity. Assess risks. Identify mitigation measures. Document assessment. Review regularly."),
            ("5. Verification", "Verify assessment is complete. Check documentation."),
            ("6. Confirmation", "Confirm impact assessment complete."),
            ("7. Documentation", "Record assessment activities."),
            ("8. Quality Control", "Review assessments for completeness. Ensure all risks are identified.")
        ]
    }
}

# ============================================================
# PDF GENERATOR
# ============================================================

def create_pdf(filepath, document_title, sections):
    filepath.parent.mkdir(parents=True, exist_ok=True)

    document = SimpleDocTemplate(
        str(filepath),
        pagesize=A4,
        rightMargin=22 * mm,
        leftMargin=22 * mm,
        topMargin=20 * mm,
        bottomMargin=20 * mm
    )

    content = []

    content.append(Paragraph(HOTEL_NAME, hotel_title))
    content.append(Paragraph(document_title, document_title_style))
    content.append(Spacer(1, 12))

    for heading, text in sections:
        content.append(Paragraph(heading, section_title))
        content.append(Paragraph(" ".join(text.split()), body))
        content.append(Spacer(1, 4))

    content.append(Spacer(1, 16))
    content.append(Paragraph(DOCUMENT_FOOTER, footer))

    document.build(content)

# ============================================================
# CREATE ALL DOCUMENTS
# ============================================================

def generate_procedures():
    for filename, document in procedures.items():
        filepath = BASE_DIR / filename
        create_pdf(filepath, document["title"], document["sections"])
        print(f"Created: {filepath}")

if __name__ == "__main__":
    generate_procedures()