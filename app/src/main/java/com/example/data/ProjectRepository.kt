package com.example.data

import com.example.model.CmsProject
import com.example.model.StudioInquiry
import com.example.model.StudioMetric
import com.example.model.StudioService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ProjectRepository {

    val CLIENTS = listOf("Ether", "Vortex", "Aura", "Zenith", "Quantum", "Eon", "Nova", "Hyperion")

    val DEFAULT_PROJECTS: List<CmsProject> = listOf(
        CmsProject(
            id = "1",
            title = "Ether Real Estate",
            category = "Web 3.0 / Luxury",
            description = "A decentralized platform for high-end property transactions, featuring smart contract integration and immersive 3D walkthroughs.",
            longOverview = "Ether Real Estate pioneers cryptographic escrow and dynamic procedural rendering for ultra-prime architectural listings. Buyers explore fractional and full-deed real assets with verified on-chain title records and zero-latency ray-marched virtual tours.",
            technologies = listOf("Solidity", "React", "Three.js", "Firebase", "Web3Modal"),
            link = "https://ether-luxury.io",
            image = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&q=80&w=1000",
            color = "#E2FF45",
            client = "Ether Holdings Group",
            latency = "< 64ms",
            uptime = "99.995%",
            security = "Smart Contract Audited / AES-256",
            platformType = "Decentralized Edge",
            architectureSummary = "Hybrid Web3 gateway combining EVM smart contract listeners with decentralized CDN nodes for instant multi-asset 3D mesh hydration.",
            keyFeatures = listOf(
                "Real-time Three.js spatial floor plan viewer",
                "Automated multi-sig escrow contract verification",
                "Biometric encrypted signature wallet authentication",
                "High-dynamic range lighting environment shaders"
            ),
            code = """// Ether Real Estate - Smart Contract Integration
import React, { Suspense } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Stage } from '@react-three/drei';

export const PropertyViewer = ({ modelPath, contractAddress }) => {
  return (
    <Canvas shadows camera={{ position: [0, 0, 150], fov: 40 }}>
      <Suspense fallback={<Loader />}>
        <Stage environment="city" intensity={0.6}>
          <Model path={modelPath} verifiedOnChain={true} />
        </Stage>
      </Suspense>
      <OrbitControls autoRotate enableZoom={true} />
    </Canvas>
  );
};"""
        ),
        CmsProject(
            id = "2",
            title = "Aura Fragrances",
            category = "E-Commerce / Branding",
            description = "An ultra-premium e-commerce experience for a luxury fragrance house, focusing on multisensory storytelling through digital design.",
            longOverview = "Aura combines bespoke olfactory narrative design with headless commerce microservices. Each perfume formulation is paired with dynamic audio-visual harmonics, custom typography interactions, and frictionless global checkout pipelines.",
            technologies = listOf("Next.js", "Shopify Headless", "Motion", "Tailwind", "Stripe"),
            link = "https://aura-essence.com",
            image = "https://images.unsplash.com/photo-1541643600914-78b084683601?auto=format&fit=crop&q=80&w=1000",
            color = "#FF4E00",
            client = "Aura Parfumerie Paris",
            latency = "< 42ms",
            uptime = "99.999%",
            security = "PCI-DSS Level 1 / TLS 1.3",
            platformType = "Headless Edge Mesh",
            architectureSummary = "Serverless Edge GraphQL layer with automated inventory orchestration and sub-50ms international checkout tokenization.",
            keyFeatures = listOf(
                "Interactive scent pyramid visualizer",
                "Custom fluid gesture motion controls",
                "Multi-currency localized tax calculation",
                "Instant edge-cached asset delivery"
            ),
            code = """// Aura Fragrances - Sensory Motion System
import { motion, AnimatePresence } from "framer-motion";

export const FragranceBottleExperience = ({ notes, activeNote }) => {
  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.96 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.8, ease: [0.16, 1, 0.3, 1] }}
      className="fragrance-showcase-container"
    >
      <AtmosphericGradient activeNote={activeNote} />
      <div className="scent-pyramid-overlay">
        {notes.map((note) => (
          <NotePill key={note.id} note={note} isActive={note.id === activeNote} />
        ))}
      </div>
    </motion.div>
  );
};"""
        ),
        CmsProject(
            id = "3",
            title = "Vortex Analytics",
            category = "Fintech / SaaS",
            description = "Real-time data visualization engine for high-frequency trading firms, processing millions of events per second with sub-millisecond latency.",
            longOverview = "Vortex Analytics ingests millions of live tick events across global order books, streaming GPU-accelerated telemetry directly to trading desks with WebAssembly and zero-copy binary serialization.",
            technologies = listOf("D3.js", "WebSockets", "Rust", "TypeScript", "Wasm"),
            link = "https://vortex-app.dev",
            image = "https://images.unsplash.com/photo-1551288049-bbbda536339a?auto=format&fit=crop&q=80&w=1000",
            color = "#00E5FF",
            client = "Vortex Capital Labs",
            latency = "< 18ms",
            uptime = "100.0%",
            security = "SOC2 Type II / Hardware HSM",
            platformType = "High-Frequency Distributed",
            architectureSummary = "Rust Wasm stream demuxer connected over persistent multiplexed WebSockets with ring-buffer caching for sub-millisecond tick render times.",
            keyFeatures = listOf(
                "60 FPS WebGL depth order book heatmaps",
                "Predictive volatility risk modeling engine",
                "Sub-millisecond latency event ingestion",
                "Customizable multi-monitor algorithmic desk layouts"
            ),
            code = """// Vortex Analytics - High-Frequency Tick Stream
import { useEffect, useRef } from 'react';

export function useOrderBookStream(symbol: string) {
  const wsRef = useRef<WebSocket | null>(null);
  
  useEffect(() => {
    const ws = new WebSocket(`wss://stream.vortex.dev/feed/v2/${'$'}{symbol}`);
    ws.binaryType = "arraybuffer";
    
    ws.onmessage = (event) => {
      const decodedBuffer = parseBinaryTickPacket(event.data);
      renderWebGLFrame(decodedBuffer);
    };
    
    return () => ws.close();
  }, [symbol]);
}"""
        ),
        CmsProject(
            id = "4",
            title = "Zenith Architecture",
            category = "Minimal / Portfolio",
            description = "A minimalist digital monograph for an award-winning architectural firm, emphasizing whitespace and structured typography.",
            longOverview = "Zenith Monograph showcases brutalist and organic architectural designs through high-resolution spatial layouts, custom typographic grids, and architectural elevation transitions.",
            technologies = listOf("Next.js", "GraphQL", "Contentful", "Motion", "Tailwind"),
            link = "https://zenith-arch.build",
            image = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&q=80&w=1000",
            color = "#A855F7",
            client = "Zenith Architekten Studio",
            latency = "< 55ms",
            uptime = "99.99%",
            security = "SSL / Content Delivery Network",
            platformType = "Static Global Edge",
            architectureSummary = "Decoupled headless CMS pipeline generating immutable static assets distributed across 280+ edge Points of Presence.",
            keyFeatures = listOf(
                "Architectural elevation drawing cross-section viewer",
                "High-contrast editorial typography scale",
                "Contentful dynamic headless publishing sync",
                "Instant offline caching with service workers"
            ),
            code = """// Zenith Architecture - Structured Monograph
import { LayoutGroup, motion } from 'framer-motion';

export const BlueprintGrid = ({ projects }) => {
  return (
    <LayoutGroup>
      <div className="grid grid-cols-12 gap-8 font-mono border-t border-black">
        {projects.map((item, index) => (
          <motion.article 
            layout 
            key={item.id}
            className="col-span-12 md:col-span-6 border-b border-r p-8"
          >
            <span className="text-xs uppercase tracking-widest">/0{index + 1}</span>
            <h2 className="text-3xl font-serif mt-4">{item.title}</h2>
          </motion.article>
        ))}
      </div>
    </LayoutGroup>
  );
};"""
        ),
        CmsProject(
            id = "5",
            title = "Quantum AI Matrix",
            category = "Artificial Intelligence / R&D",
            description = "Next-generation multimodal reasoning dashboard and neural pipeline orchestrator for autonomous agent fleets.",
            longOverview = "Quantum AI Matrix delivers stateful inspection of large reasoning models, latency-profiled token pipelines, and real-time execution graphs for enterprise autonomous agent swarms.",
            technologies = listOf("Kotlin", "Gemini API", "Python", "WebGPU", "gRPC"),
            link = "https://quantum-matrix.ai",
            image = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&q=80&w=1000",
            color = "#10B981",
            client = "Quantum Cognitive Systems",
            latency = "< 35ms",
            uptime = "99.999%",
            security = "Enterprise IAM / Zero Trust",
            platformType = "Hybrid Compute Mesh",
            architectureSummary = "Dynamic token streaming gateway with low-latency GPU acceleration and zero-trust cryptographic audit trails.",
            keyFeatures = listOf(
                "Live token velocity and attention weight heatmaps",
                "Multimodal tool invocation tracer",
                "Sub-40ms speculative reasoning streaming",
                "Enterprise fleet safety guardrails"
            ),
            code = """// Quantum Matrix - Autonomous Agent Pipeline
import com.google.ai.client.generativeai.GenerativeModel

class AgentReasoningOrchestrator(
    private val model: GenerativeModel
) {
    suspend fun executeReasoningCycle(prompt: String): Flow<TokenTelemetry> = flow {
        val stream = model.generateContentStream(prompt)
        stream.collect { chunk ->
            emit(TokenTelemetry(text = chunk.text ?: "", latencyMs = measureTick()))
        }
    }
}"""
        ),
        CmsProject(
            id = "6",
            title = "Nova Dynamics",
            category = "Aerospace / Telemetry",
            description = "Orbital mission control telemetry interface and mission telemetry visualization for next-generation aerospace operations.",
            longOverview = "Nova Dynamics engineers mission-critical avionics dashboards with real-time trajectory computations, satellite constellation orbital tracking, and automated emergency contingency scripts.",
            technologies = listOf("WebGL", "Cesium.js", "Go", "WebRTC", "Protobuf"),
            link = "https://nova-orbital.space",
            image = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&q=80&w=1000",
            color = "#FF2E63",
            client = "Nova Space Consortium",
            latency = "< 24ms",
            uptime = "100.0%",
            security = "Military-grade End-to-End Encryption",
            platformType = "Mission Critical Edge",
            architectureSummary = "Redundant multi-satellite downlink ingestion with sub-30ms coordinate projections and hardware-synced telemetry.",
            keyFeatures = listOf(
                "3D orbital trajectory simulation",
                "Real-time constellation state sync",
                "Instant telemetry delta anomaly detection",
                "Failsafe hardware command pipelines"
            ),
            code = """// Nova Dynamics - Orbital Telemetry Mesh
import { Vector3, Quaternion } from 'three';

export class TrajectoryCalculator {
  static computeEphemeris(orbitParams: OrbitState, timestamp: number): Vector3 {
    const meanAnomaly = computeMeanAnomaly(orbitParams, timestamp);
    const eccentricAnomaly = solveKepler(meanAnomaly, orbitParams.eccentricity);
    return projectOrbitalPlane(eccentricAnomaly, orbitParams);
  }
}"""
        ),
        CmsProject(
            id = "7",
            title = "Hyperion Spatial",
            category = "Digital Experience / AR",
            description = "Immersive spatial web computing platform delivering interactive 3D volumetric environments and spatial audio for architectural galleries.",
            longOverview = "Hyperion Spatial engineers next-generation WebSpatial experiences with zero-install WebXR streaming, photorealistic lighting irradiance fields, and synchronized multi-user architectural walkthroughs.",
            technologies = listOf("WebXR", "Three.js", "WebAudio", "TypeScript", "GLSL"),
            link = "https://hyperion-spatial.studio",
            image = "https://images.unsplash.com/photo-1506157786151-b8491531f063?auto=format&fit=crop&q=80&w=1000",
            color = "#005AC1",
            client = "Hyperion Spatial Labs",
            latency = "< 28ms",
            uptime = "99.998%",
            security = "Zero-Knowledge Biometric Key / TLS 1.3",
            platformType = "Spatial Web Mesh",
            architectureSummary = "Distributed WebXR streaming pipeline with volumetric point-cloud level-of-detail compression and spatialized HRTF binaural audio synthesis.",
            keyFeatures = listOf(
                "Real-time WebXR spatial canvas rendering",
                "Volumetric point-cloud dynamic LOD streaming",
                "Spatial HRTF binaural audio acoustics engine",
                "Cross-platform headset and mobile sync"
            ),
            code = """// Hyperion Spatial - Volumetric Audio-Visual Mesh
import { AudioContext, SpatialPannerNode } from 'standardized-audio-context';

export class SpatialAcousticEngine {
  private ctx: AudioContext;
  
  constructor() {
    this.ctx = new AudioContext();
  }
  
  bindArchitecturalReverb(roomDimensions: Vector3) {
    const impulse = generateImpulseResponse(roomDimensions, 0.45);
    return this.ctx.createConvolver().buffer = impulse;
  }
}"""
        ),
        CmsProject(
            id = "8",
            title = "Monolith Architecture",
            category = "Architecture / BIM",
            description = "Digital twin and architectural monograph platform with real-time solar path ray tracing and parametric façade simulations.",
            longOverview = "Monolith Studio delivers high-precision architectural visualization integrating Building Information Modeling (BIM) datasets with real-time physics, shadow simulations, and editorial material monographs.",
            technologies = listOf("React", "Three.js", "WebGPU", "IFC.js", "Tailwind"),
            link = "https://monolith-arch.design",
            image = "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&q=80&w=1000",
            color = "#001D3D",
            client = "Monolith Atelier Zurich",
            latency = "< 38ms",
            uptime = "100.0%",
            security = "SOC2 Type II / AES-256",
            platformType = "Parametric Digital Twin",
            architectureSummary = "Client-side WebGPU acceleration for real-time solar ray casting and IFC BIM model parsing with zero server overhead.",
            keyFeatures = listOf(
                "Real-time solar shadow & azimuth simulation",
                "IFC BIM structural data parser & layer inspection",
                "Editorial monochrome architectural photography mode",
                "High-dynamic parametric geometry calculation"
            ),
            code = """// Monolith Architecture - Solar Ray Calculation
export function calculateSolarAzimuth(lat: number, lon: number, date: Date) {
  const julianDay = toJulianDate(date);
  const solarTime = computeLocalSolarTime(julianDay, lon);
  const declination = computeSolarDeclination(julianDay);
  return {
    altitude: Math.asin(Math.sin(lat) * Math.sin(declination)),
    azimuth: Math.acos(Math.cos(declination) / Math.cos(lat))
  };
}"""
        )
    )

    val STUDIO_SERVICES = listOf(
        StudioService(
            id = "s1",
            title = "Digital Experience Architecture",
            tagline = "Immersive, high-performance web & mobile platforms.",
            description = "We design and engineer bespoke digital flagship experiences with fluid 60fps micro-interactions, responsive typography, and high-impact visual aesthetics.",
            deliverables = listOf("Complete Design System", "High-Fidelity Native / Web App", "Interactive 3D Elements", "Full Responsive Engine"),
            iconName = "globe",
            accentHex = "#E2FF45"
        ),
        StudioService(
            id = "s2",
            title = "Headless & Luxury Commerce",
            tagline = "Ultra-fast transactional systems engineered to convert.",
            description = "Decoupled commerce architectures built on Shopify Headless, Next.js, and Stripe for global brands demanding sub-second load speeds and flawless security.",
            deliverables = listOf("Headless Shopify Core", "Global Multi-Currency Checkout", "Instant Edge Caching", "Custom Product Builders"),
            iconName = "shopping-cart",
            accentHex = "#FF4E00"
        ),
        StudioService(
            id = "s3",
            title = "High-Frequency Analytics & Fintech",
            tagline = "Zero-latency real-time data pipelines & visualization.",
            description = "Complex telemetry dashboards, WebGL financial charts, and streaming WebSockets designed for high-density information display without frame drops.",
            deliverables = listOf("WebSocket Stream Engine", "Custom D3 / WebGL Charts", "Order Book Heatmaps", "Role-Based IAM Security"),
            iconName = "activity",
            accentHex = "#00E5FF"
        ),
        StudioService(
            id = "s4",
            title = "AI & Autonomous Agent Systems",
            tagline = "Enterprise generative intelligence & agentic workflows.",
            description = "Integrate multimodal foundation models, Gemini API pipelines, vector databases, and automated orchestration workflows into scalable enterprise software.",
            deliverables = listOf("Gemini Multimodal Pipelines", "Custom Tool Integrations", "Latency Optimization", "Agentic Fleet Guardrails"),
            iconName = "cpu",
            accentHex = "#10B981"
        )
    )

    val STUDIO_METRICS = listOf(
        StudioMetric("LATENCY TARGET", "< 45ms", "-12ms vs avg", "Global Edge CDN routing with warm compute nodes"),
        StudioMetric("UPTIME INTEGRITY", "99.99%", "Zero outages", "Continuous multi-region automated health verification"),
        StudioMetric("SECURITY LAYER", "AES-256", "Grade A+", "End-to-end cryptographic tokenization and SSL"),
        StudioMetric("DEPLOYMENT SPEED", "1.8s", "Atomic push", "Decoupled CI/CD container pipeline automation")
    )

    private val _projects = MutableStateFlow<List<CmsProject>>(DEFAULT_PROJECTS)
    val projects: StateFlow<List<CmsProject>> = _projects.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(setOf("1", "3"))
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _inquiries = MutableStateFlow<List<StudioInquiry>>(emptyList())
    val inquiries: StateFlow<List<StudioInquiry>> = _inquiries.asStateFlow()

    private val _cmsStatus = MutableStateFlow("ACTIVE (HYBRID SYNC)")
    val cmsStatus: StateFlow<String> = _cmsStatus.asStateFlow()

    fun toggleFavorite(projectId: String) {
        _favoriteIds.update { current ->
            if (current.contains(projectId)) {
                current - projectId
            } else {
                current + projectId
            }
        }
    }

    fun submitInquiry(inquiry: StudioInquiry): Boolean {
        _inquiries.update { listOf(inquiry) + it }
        return true
    }

    fun getProjectById(id: String): CmsProject? {
        return _projects.value.find { it.id == id }
    }
}
