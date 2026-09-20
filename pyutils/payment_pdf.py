import sys
from pathlib import Path
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.units import mm
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer
from reportlab.lib import colors

HOTEL_NAME = "Maison Grand Hotel"
HOTEL_ADDRESS = "08 Greenwood Street, Gardens, Cape Town, 7700, South Africa"
HOTEL_PHONE = "+27 21 987 6543"
HOTEL_EMAIL = "stay@maisongrandhotel.co.za"

def main():
    txn_id, guest, reference, method, amount, date, status, out_dir = sys.argv[1:9]

    out_dir = Path(out_dir)
    out_dir.mkdir(parents=True, exist_ok=True)
    out_file = out_dir / f"payment-{txn_id}.pdf"

    styles = getSampleStyleSheet()

    hotel_title = ParagraphStyle("HotelTitle", parent=styles["Title"], alignment=TA_CENTER, fontSize=20, leading=24, spaceAfter=4, textColor=colors.HexColor("#1a1814"))
    doc_title = ParagraphStyle("DocTitle", parent=styles["Title"], alignment=TA_CENTER, fontSize=16, leading=20, spaceAfter=24, textColor=colors.HexColor("#c9a84c"))
    row_key = ParagraphStyle("RowKey", parent=styles["BodyText"], fontSize=10.5, leading=15, textColor=colors.HexColor("#666666"))
    row_val = ParagraphStyle("RowVal", parent=styles["BodyText"], fontSize=11, leading=15, textColor=colors.HexColor("#1a1814"))
    footer = ParagraphStyle("Footer", parent=styles["BodyText"], fontSize=8.5, leading=12, spaceBefore=24, textColor=colors.HexColor("#666666"))

    doc = SimpleDocTemplate(str(out_file), pagesize=A4, rightMargin=22*mm, leftMargin=22*mm, topMargin=20*mm, bottomMargin=20*mm)

    content = []
    content.append(Paragraph(HOTEL_NAME, hotel_title))
    content.append(Paragraph("Payment Record", doc_title))
    content.append(Spacer(1, 12))

    for key, value in [
        ("Transaction ID", txn_id),
        ("Guest", guest),
        ("Reference", reference),
        ("Method", method),
        ("Amount", amount),
        ("Date", date),
        ("Status", status),
    ]:
        content.append(Paragraph(key, row_key))
        content.append(Paragraph(str(value), row_val))
        content.append(Spacer(1, 6))

    content.append(Spacer(1, 16))
    content.append(Paragraph(
        f"{HOTEL_NAME}<br/>{HOTEL_ADDRESS}<br/>{HOTEL_PHONE} | {HOTEL_EMAIL}",
        footer
    ))

    doc.build(content)
    print(f"Created: {out_file}")

if __name__ == "__main__":
    main()