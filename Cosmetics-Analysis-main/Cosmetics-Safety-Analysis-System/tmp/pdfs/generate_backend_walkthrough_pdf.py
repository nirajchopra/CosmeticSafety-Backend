from pathlib import Path

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus import ListFlowable, ListItem, PageBreak, Paragraph, SimpleDocTemplate, Spacer, Table, TableStyle


OUTPUT_PATH = Path(
    "/Users/sushobhit/eclipse-workspace/Cosmetics Safety Analysis System/Cosmetics-Safety-Analysis-System/output/pdf/backend-walkthrough.pdf"
)


def bullet_list(items, style):
    return ListFlowable(
        [ListItem(Paragraph(item, style)) for item in items],
        bulletType="bullet",
        start="circle",
        leftIndent=18,
    )


def build_pdf():
    OUTPUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    doc = SimpleDocTemplate(
        str(OUTPUT_PATH),
        pagesize=A4,
        rightMargin=40,
        leftMargin=40,
        topMargin=45,
        bottomMargin=45,
        title="Cosmetics Safety Analysis System Backend Walkthrough",
        author="OpenAI Codex",
    )

    styles = getSampleStyleSheet()
    title_style = ParagraphStyle(
        "TitleCustom",
        parent=styles["Title"],
        alignment=TA_CENTER,
        fontSize=20,
        leading=24,
        textColor=colors.HexColor("#16324f"),
        spaceAfter=16,
    )
    heading_style = ParagraphStyle(
        "HeadingCustom",
        parent=styles["Heading2"],
        fontSize=14,
        leading=18,
        textColor=colors.HexColor("#1f4e79"),
        spaceAfter=8,
        spaceBefore=10,
    )
    body_style = ParagraphStyle(
        "BodyCustom",
        parent=styles["BodyText"],
        fontSize=10,
        leading=14,
        spaceAfter=6,
    )
    small_style = ParagraphStyle(
        "SmallCustom",
        parent=styles["BodyText"],
        fontSize=9,
        leading=12,
        textColor=colors.HexColor("#444444"),
    )

    story = []
    story.append(Paragraph("Cosmetics Safety Analysis System", title_style))
    story.append(Paragraph("Backend Walkthrough Document", title_style))
    story.append(
        Paragraph(
            "This document explains the current backend design in simple English. "
            "It is written for project review, onboarding, and quick understanding.",
            body_style,
        )
    )
    story.append(Spacer(1, 0.15 * inch))

    story.append(Paragraph("1. Project Overview", heading_style))
    overview_points = [
        "Backend technology: Spring Boot 2.7.x with Java 11.",
        "Database setup: MySQL for the main application and H2 only for tests.",
        "Authentication style: JWT-based stateless security.",
        "Current business coverage: Auth, User, Role, Complaint, Brand, Product, and Ingredient modules.",
        "Planned next modules: ProductIngredient, Report, Import, and Notification.",
    ]
    story.append(bullet_list(overview_points, body_style))

    story.append(Paragraph("2. Package Structure", heading_style))
    structure_data = [
        ["Package", "Purpose"],
        ["com.cosmeticssafety.config", "Security setup, startup data, and configuration classes"],
        ["com.cosmeticssafety.controller", "REST API controllers"],
        ["com.cosmeticssafety.service", "Service interfaces for business rules"],
        ["com.cosmeticssafety.service.impl", "Service implementations"],
        ["com.cosmeticssafety.repository", "Spring Data JPA repositories"],
        ["com.cosmeticssafety.entity", "Database entity classes"],
        ["com.cosmeticssafety.dto", "Request and response objects"],
        ["com.cosmeticssafety.security", "JWT and user authentication classes"],
        ["com.cosmeticssafety.exception", "Global exception handling and custom exceptions"],
        ["com.cosmeticssafety.common.enums", "Shared enums such as roles and risk levels"],
        ["com.cosmeticssafety.common.constants", "Shared constant values like API version paths"],
    ]
    structure_table = Table(structure_data, colWidths=[2.2 * inch, 4.6 * inch])
    structure_table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#d9e8f5")),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.HexColor("#16324f")),
                ("GRID", (0, 0), (-1, -1), 0.5, colors.grey),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("PADDING", (0, 0), (-1, -1), 6),
            ]
        )
    )
    story.append(structure_table)

    story.append(Paragraph("3. Module Explanation", heading_style))
    module_points = [
        "<b>Auth Module</b>: Handles registration, login, JWT token generation, forgot password, reset password, and change password.",
        "<b>User Module</b>: Handles user profile viewing, profile update, account enable or disable, lock or unlock, and admin-side user listing.",
        "<b>Role Module</b>: Uses ADMIN, ANALYST, and CONSUMER roles. Role assignment is controlled by admin APIs.",
        "<b>Complaint Module</b>: Lets consumers create complaints. Admin and analyst users can review and update complaint status.",
        "<b>Brand Module</b>: Manages cosmetic brands with active and inactive status.",
        "<b>Product Module</b>: Manages shampoo products and links each product to one brand.",
        "<b>Ingredient Module</b>: Stores ingredient master data, risk level, side effects, references, and allowed status.",
    ]
    story.append(bullet_list(module_points, body_style))

    story.append(PageBreak())
    story.append(Paragraph("4. Current API List", heading_style))
    api_points = [
        "<b>Auth APIs</b>: POST /api/v1/auth/register, POST /api/v1/auth/login, POST /api/v1/auth/forgot-password, POST /api/v1/auth/reset-password, PUT /api/v1/auth/change-password",
        "<b>User APIs</b>: GET /api/v1/users/me, PUT /api/v1/users/me, PUT /api/v1/users/me/change-password, GET /api/v1/users, PUT /api/v1/users/{userId}/status, PUT /api/v1/users/{userId}/roles",
        "<b>Complaint APIs</b>: POST /api/v1/complaints, GET /api/v1/complaints/my, GET /api/v1/complaints, PUT /api/v1/complaints/{complaintId}/status",
        "<b>Brand APIs</b>: POST /api/v1/brands, PUT /api/v1/brands/{brandId}, GET /api/v1/brands",
        "<b>Product APIs</b>: POST /api/v1/products, PUT /api/v1/products/{productId}, GET /api/v1/products",
        "<b>Ingredient APIs</b>: POST /api/v1/ingredients, PUT /api/v1/ingredients/{ingredientId}, GET /api/v1/ingredients",
        "<b>Utility API</b>: GET /api/v1/health",
    ]
    story.append(bullet_list(api_points, body_style))

    story.append(Paragraph("5. Database Entity Relations", heading_style))
    relation_points = [
        "One user can have many roles and one role can be assigned to many users.",
        "One complaint belongs to one user as the submitter.",
        "One product belongs to one brand.",
        "Ingredients are currently standalone master records.",
        "The ProductIngredient module is planned for the next phase and will create the many-to-many link between products and ingredients.",
    ]
    story.append(bullet_list(relation_points, body_style))

    story.append(Paragraph("6. Auth and Role Flow", heading_style))
    auth_points = [
        "A user registers or logs in using email and password.",
        "The backend validates credentials and returns a JWT token.",
        "The client sends the token in the Authorization header as Bearer token.",
        "The JWT filter reads the token, loads the user, and sets the security context.",
        "Role checks are enforced using method-level security such as hasRole and hasAnyRole.",
        "Admin can assign roles. Analyst is used for review and analysis work. Consumer is used for end-user features.",
    ]
    story.append(bullet_list(auth_points, body_style))

    story.append(Paragraph("7. Design Decisions", heading_style))
    design_points = [
        "The project follows a layered design: controller -> service -> repository -> database.",
        "DTO classes are used so that API payloads stay clean and are separate from entities.",
        "Global exception handling gives consistent JSON error responses.",
        "Profile-based datasource setup keeps production and testing separated.",
        "The current implementation is modular so future modules can be added without rewriting the base structure.",
    ]
    story.append(bullet_list(design_points, body_style))

    story.append(PageBreak())
    story.append(Paragraph("8. Current Gaps", heading_style))
    gap_points = [
        "ProductIngredient module is not implemented yet.",
        "Report module is not implemented yet.",
        "Excel Import module is not implemented yet.",
        "Notification module is not implemented yet.",
        "Swagger or OpenAPI setup is only a placeholder today.",
        "Forgot password currently returns token in response for development. Email integration is still pending.",
    ]
    story.append(bullet_list(gap_points, body_style))

    story.append(Paragraph("9. Next Implementation Roadmap", heading_style))
    roadmap_points = [
        "Create ProductIngredient mapping with concentration fields.",
        "Create Report module for ingredient analysis and severity output.",
        "Add Excel import for bulk ingredient and product data.",
        "Add email integration for forgot password and complaint updates.",
        "Create Angular frontend screens and connect them to current APIs.",
    ]
    story.append(bullet_list(roadmap_points, body_style))

    story.append(Spacer(1, 0.18 * inch))
    story.append(
        Paragraph(
            "Generated for project documentation inside the workspace. "
            "This file can be shared with team members for quick onboarding.",
            small_style,
        )
    )

    doc.build(story)


if __name__ == "__main__":
    build_pdf()
